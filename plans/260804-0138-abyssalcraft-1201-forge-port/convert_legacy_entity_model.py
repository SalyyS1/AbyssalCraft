"""Convert 1.12.2 ModelBase subclasses into 1.20.1 LayerDefinition code.

The 1.12.2 models declare geometry imperatively in the constructor:

    part = new ModelRenderer(this, texU, texV);
    part.addBox(x, y, z, w, h, d);
    part.setRotationPoint(px, py, pz);
    setRotation(part, rx, ry, rz);

1.20.1 wants the same numbers as a static MeshDefinition. The mapping is mechanical, so this
emits the PartDefinition calls and the renderToBuffer part list for a given legacy model, which
is then pasted into a hand-written model class. Animation code is NOT converted: setRotationAngles
bodies vary too much to translate blindly and are ported by hand.
"""
import pathlib
import re
import sys


def snake(name):
    """Body1 -> body1, RightArm -> right_arm."""
    out = re.sub(r"(?<!^)(?=[A-Z])", "_", name).lower()
    return re.sub(r"_+", "_", out)


def convert(path):
    source = path.read_text(encoding="utf-8")

    size = re.search(r"textureWidth\s*=\s*(\d+);\s*textureHeight\s*=\s*(\d+);", source)
    tex_w, tex_h = (size.group(1), size.group(2)) if size else ("64", "32")

    # part = new ModelRenderer(this, u, v);
    tex_offsets = {
        name: (u, v)
        for name, u, v in re.findall(r"(\w+)\s*=\s*new ModelRenderer\(this,\s*(-?\d+),\s*(-?\d+)\)", source)
    }
    boxes = {
        name: nums
        for name, nums in re.findall(r"(\w+)\.addBox\(([^)]+)\)", source)
    }
    pivots = {
        name: nums
        for name, nums in re.findall(r"(\w+)\.setRotationPoint\(([^)]+)\)", source)
    }
    rotations = {
        name: nums
        for name, nums in re.findall(r"setRotation\((\w+),\s*([^)]+)\)", source)
    }

    def clean(values):
        return [v.strip().rstrip("Ff") for v in values.split(",")]

    lines = []
    for name in boxes:
        u, v = tex_offsets.get(name, ("0", "0"))
        box = clean(boxes[name])
        pivot = clean(pivots.get(name, "0,0,0"))
        rot = clean(rotations.get(name, "0,0,0"))

        cube = (
            f'CubeListBuilder.create().texOffs({u}, {v})'
            f'.addBox({box[0]}F, {box[1]}F, {box[2]}F, {box[3]}F, {box[4]}F, {box[5]}F)'
        )
        if any(float(r) != 0.0 for r in rot):
            pose = (
                f"PartPose.offsetAndRotation({pivot[0]}F, {pivot[1]}F, {pivot[2]}F, "
                f"{rot[0]}F, {rot[1]}F, {rot[2]}F)"
            )
        else:
            pose = f"PartPose.offset({pivot[0]}F, {pivot[1]}F, {pivot[2]}F)"

        lines.append(f'        root.addOrReplaceChild("{snake(name)}",')
        lines.append(f"                {cube},")
        lines.append(f"                {pose});")

    print(f"// {path.name}: texture {tex_w}x{tex_h}, {len(boxes)} cubes")
    print("\n".join(lines))
    print(f"\n        return LayerDefinition.create(mesh, {tex_w}, {tex_h});")
    print("\n// fields:")
    for name in boxes:
        print(f'    private final ModelPart {snake(name).replace("_", "")};')
    print("\n// part names for renderToBuffer:")
    print("    " + ", ".join(f'"{snake(n)}"' for n in boxes))


if __name__ == "__main__":
    convert(pathlib.Path(sys.argv[1]))
