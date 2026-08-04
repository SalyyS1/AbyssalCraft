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
import com.shinoow.abyssalcraft.common.entity.ShadowMonster;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * The ShadowMonster's model.
 * <p>
 * Geometry was converted mechanically from the 1.12.2 model constructor, so every box size, pivot
 * and pre-set rotation is the original value. The head tracks the look direction and the limbs
 * swing, matching the original animation.
 */
public class ShadowMonsterModel extends EntityModel<ShadowMonster> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftLimb;
    private final ModelPart rightLimb;

    public ShadowMonsterModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head");
        leftLimb = root.getChild("larm2");
        rightLimb = root.getChild("rarm2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -7F, -3.5F, 7F, 7F, 7F),
                PartPose.offset(0F, 0F, 0F));
        root.addOrReplaceChild("body1",
                CubeListBuilder.create().texOffs(20, 14).addBox(-1.5F, -1F, -1.5F, 3F, 10F, 3F),
                PartPose.offsetAndRotation(0F, 0F, 0F, 0.4461433F, 0F, 0F));
        root.addOrReplaceChild("body2",
                CubeListBuilder.create().texOffs(28, 0).addBox(0F, 0F, 0F, 3F, 6F, 3F),
                PartPose.offset(-1.5F, 7F, 2F));
        root.addOrReplaceChild("body3",
                CubeListBuilder.create().texOffs(28, 0).addBox(0F, 0F, 0F, 3F, 6F, 3F),
                PartPose.offsetAndRotation(-1.5F, 11F, 2.5F, -0.5948578F, 0F, 0F));
        root.addOrReplaceChild("lshoulder1",
                CubeListBuilder.create().texOffs(40, 0).addBox(0F, 0F, 0F, 3F, 2F, 2F),
                PartPose.offset(1.5F, 0F, 0F));
        root.addOrReplaceChild("rshoulder1",
                CubeListBuilder.create().texOffs(40, 0).addBox(0F, 0F, 0F, 3F, 2F, 2F),
                PartPose.offset(-4.5F, 0F, 0F));
        root.addOrReplaceChild("lshoulder2",
                CubeListBuilder.create().texOffs(40, 0).addBox(0F, 0F, 0F, 3F, 2F, 2F),
                PartPose.offset(1.5F, 5F, 2F));
        root.addOrReplaceChild("rshoulder2",
                CubeListBuilder.create().texOffs(40, 0).addBox(0F, 0F, 0F, 3F, 2F, 2F),
                PartPose.offset(-4.5F, 5F, 2F));
        root.addOrReplaceChild("larm1",
                CubeListBuilder.create().texOffs(0, 14).addBox(0F, -1F, -7F, 2F, 2F, 8F),
                PartPose.offset(4.5F, 1F, 1F));
        root.addOrReplaceChild("larm2",
                CubeListBuilder.create().texOffs(0, 14).addBox(0F, -1F, -7F, 2F, 2F, 8F),
                PartPose.offset(4.5F, 6F, 3F));
        root.addOrReplaceChild("rarm1",
                CubeListBuilder.create().texOffs(0, 14).addBox(-2F, -1F, -7F, 2F, 2F, 8F),
                PartPose.offset(-4.5F, 1F, 1F));
        root.addOrReplaceChild("rarm2",
                CubeListBuilder.create().texOffs(0, 14).addBox(-2F, -1F, -7F, 2F, 2F, 8F),
                PartPose.offset(-4.5F, 6F, 3F));
        root.addOrReplaceChild("back1",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 2F, 1F),
                PartPose.offsetAndRotation(-0.5F, 2F, 4F, -1.041002F, 0F, 0F));
        root.addOrReplaceChild("back2",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 2F, 1F),
                PartPose.offsetAndRotation(-0.5F, 4F, 5F, -1.041002F, 0F, 0F));
        root.addOrReplaceChild("back3",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 2F, 1F),
                PartPose.offsetAndRotation(-0.5F, 0F, 3F, -1.041002F, 0F, 0F));
        root.addOrReplaceChild("back4",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 1F, 2F),
                PartPose.offset(-0.5F, 8F, 4.5F));
        root.addOrReplaceChild("back5",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 1F, 2F),
                PartPose.offset(-0.5F, 10F, 4.5F));
        root.addOrReplaceChild("back6",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 1F, 2F),
                PartPose.offset(-0.5F, 12F, 4.5F));
        root.addOrReplaceChild("back7",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 2F, 1F),
                PartPose.offsetAndRotation(-0.5F, 14F, 3.5F, 1.00382F, 0F, 0F));
        root.addOrReplaceChild("back8",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 2F, 1F),
                PartPose.offsetAndRotation(-0.5F, 15.5F, 2.5F, 1.00382F, 0F, 0F));
        root.addOrReplaceChild("back9",
                CubeListBuilder.create().texOffs(50, 0).addBox(0F, 0F, 0F, 1F, 2F, 1F),
                PartPose.offsetAndRotation(-0.5F, 17F, 1.5F, 1.00382F, 0F, 0F));

        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(ShadowMonster entity, float limbSwing, float limbSwingAmount, float ageInTicks,
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
