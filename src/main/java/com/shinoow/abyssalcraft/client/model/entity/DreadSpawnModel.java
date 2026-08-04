/*******************************************************************************
 * AbyssalCraft
 * Copyright (c) 2012 - 2025 Shinoow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Contributors:
 *     Shinoow -  implementation
 ******************************************************************************/
package com.shinoow.abyssalcraft.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.shinoow.abyssalcraft.common.entity.DreadSpawn;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * The DreadSpawn's model.
 * <p>
 * Geometry was converted mechanically from the 1.12.2 model constructor, so every box size, pivot
 * and pre-set rotation is the original value. The head tracks the look direction and the limbs
 * swing, matching the original animation.
 */
public class DreadSpawnModel extends EntityModel<DreadSpawn> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftLimb;
    private final ModelPart rightLimb;

    public DreadSpawnModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head");
        leftLimb = root.getChild("arm2");
        rightLimb = root.getChild("arm3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 17).addBox(-3F, -3F, -3F, 6F, 5F, 6F),
                PartPose.offset(0F, 22F, 0F));
        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 4).addBox(-1.5F, -4F, -1.5F, 3F, 3F, 3F),
                PartPose.offsetAndRotation(0F, 19F, 0F, -0.4833219F, 0F, 0F));
        root.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(12, 4).addBox(-1.5F, -1F, -1.5F, 3F, 1F, 3F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0.4833219F, 0F, 0F));
        root.addOrReplaceChild("thing",
                CubeListBuilder.create().texOffs(0, 10).addBox(0F, -1F, -3F, 6F, 1F, 6F),
                PartPose.offset(3F, 21F, 2F));
        root.addOrReplaceChild("t1",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offsetAndRotation(4F, -1F, 2F, 0F, 0.3F, 0F));
        root.addOrReplaceChild("t11",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t12",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t13",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t2",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offsetAndRotation(4F, -1F, 4F, 0F, 0.7F, 0F));
        root.addOrReplaceChild("t21",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t22",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t23",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t3",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offsetAndRotation(1F, -1F, 0F, 0F, -0.3F, 0F));
        root.addOrReplaceChild("t31",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t32",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("t33",
                CubeListBuilder.create().texOffs(10, 5).addBox(0F, -1F, -3F, 1F, 1F, 1F),
                PartPose.offset(0F, -1F, 0F));
        root.addOrReplaceChild("arm",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -1F, -3F, 2F, 2F, 2F),
                PartPose.offset(-2F, 22F, 0F));
        root.addOrReplaceChild("arm1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-1F, -1F, -3F, 2F, 2F, 2F),
                PartPose.offset(0F, 0F, -2F));
        root.addOrReplaceChild("arm2",
                CubeListBuilder.create().texOffs(8, 0).addBox(-1F, -1F, -2F, 2F, 2F, 2F),
                PartPose.offset(0F, 0F, -3F));
        root.addOrReplaceChild("arm3",
                CubeListBuilder.create().texOffs(16, 0).addBox(-1F, -1F, -2F, 2F, 2F, 2F),
                PartPose.offset(0F, 0F, -2F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(DreadSpawn entity, float limbSwing, float limbSwingAmount, float ageInTicks,
            float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;
        rightLimb.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F;
        leftLimb.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int packedLight,
            int packedOverlay, float red, float green, float blue, float alpha) {
        // Rendering the root recurses into every child, so parts are drawn exactly once.
        root.render(pose, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
