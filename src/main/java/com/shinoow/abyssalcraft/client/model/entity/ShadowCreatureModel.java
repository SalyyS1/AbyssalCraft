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
import com.shinoow.abyssalcraft.common.entity.ShadowCreature;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * The Shadow Creature's model: a low quadruped body with a long segmented tail.
 * <p>
 * Geometry was converted mechanically from the 1.12.2 {@code ModelShadowCreature} constructor, so
 * every box size, pivot and pre-set rotation is the original value. Only the head follows the look
 * direction and only the forelimbs swing, matching the original animation.
 */
public class ShadowCreatureModel extends EntityModel<ShadowCreature> {

    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart leftForelimb;
    private final ModelPart rightForelimb;

    public ShadowCreatureModel(ModelPart root) {
        this.root = root;
        head = root.getChild("head1");
        leftForelimb = root.getChild("left_arm2");
        rightForelimb = root.getChild("right_arm2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(12, 22).addBox(0F, 0F, 0F, 3F, 3F, 7F),
                PartPose.offset(-3F, 12F, -1F));
        root.addOrReplaceChild("head1",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3F, -5F, -3F, 5F, 5F, 5F),
                PartPose.offset(-1F, 12F, 0F));
        root.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(18, 15).addBox(0F, 0F, 0F, 2F, 2F, 5F),
                PartPose.offsetAndRotation(-2.466667F, 12.5F, 6F, -0.3717861F, 0F, 0F));
        root.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(26, 12).addBox(0F, 0F, 0F, 1F, 1F, 2F),
                PartPose.offsetAndRotation(-2F, 14F, 9F, 1.115358F, 0F, 0F));
        root.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(23, 7).addBox(0F, 0F, 0F, 3F, 1F, 1F),
                PartPose.offsetAndRotation(-1F, 14.5F, 9F, -0.4089647F, 0F, 0F));
        root.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(23, 7).addBox(0F, 0F, 0F, 3F, 1F, 1F),
                PartPose.offsetAndRotation(-1F, 13.5F, 7F, -0.4089647F, 0F, 0F));
        root.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(23, 7).addBox(0F, 0F, 0F, 3F, 1F, 1F),
                PartPose.offsetAndRotation(-5F, 14.5F, 9F, -0.4089647F, 0F, 0F));
        root.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(23, 7).addBox(0F, 0F, 0F, 3F, 1F, 1F),
                PartPose.offsetAndRotation(-5F, 13.5F, 7F, -0.4089647F, 0F, 0F));
        root.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(26, 12).addBox(0F, 0F, 0F, 1F, 1F, 2F),
                PartPose.offsetAndRotation(-2F, 13F, 7F, 1.115358F, 0F, 0F));
        root.addOrReplaceChild("left_arm1",
                CubeListBuilder.create().texOffs(11, 19).addBox(0F, 0F, 0F, 2F, 1F, 1F),
                PartPose.offset(0F, 13F, 0F));
        root.addOrReplaceChild("right_arm1",
                CubeListBuilder.create().texOffs(11, 19).addBox(0F, 0F, 0F, 2F, 1F, 1F),
                PartPose.offset(-5F, 13F, 0F));
        root.addOrReplaceChild("left_arm2",
                CubeListBuilder.create().texOffs(0, 22).addBox(0F, 0F, -5F, 1F, 1F, 5F),
                PartPose.offset(1F, 13F, 0F));
        root.addOrReplaceChild("right_arm2",
                CubeListBuilder.create().texOffs(0, 22).addBox(0F, 0F, -5F, 1F, 1F, 5F),
                PartPose.offset(-5F, 13F, 0F));

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(ShadowCreature entity, float limbSwing, float limbSwingAmount,
            float ageInTicks, float netHeadYaw, float headPitch) {
        head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        head.xRot = headPitch * Mth.DEG_TO_RAD;
        rightForelimb.xRot = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 2.0F * limbSwingAmount * 0.5F;
        leftForelimb.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
    }

    @Override
    public void renderToBuffer(PoseStack pose, VertexConsumer buffer, int packedLight,
            int packedOverlay, float red, float green, float blue, float alpha) {
        // Rendering the root recurses into every child, so parts are drawn exactly once.
        root.render(pose, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
