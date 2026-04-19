package com.breakinblocks.auroral.client.renderer;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.item.ShimmerweaveGogglesItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ShimmerweaveVisorLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>>
        extends RenderLayer<S, M> {

    private static final Identifier VISOR_TEXTURE =
        Auroral.id("textures/entity/equipment/humanoid/shimmerweave_visor.png");
    private static final RenderType VISOR_RENDER_TYPE = RenderTypes.entityTranslucent(VISOR_TEXTURE);

    private final HumanoidModel<S> visorModel;

    public ShimmerweaveVisorLayer(RenderLayerParent<S, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.visorModel = new HumanoidModel<>(modelSet.bakeLayer(ModelLayers.PLAYER_ARMOR.get(EquipmentSlot.HEAD)));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, S state,
                       float yRot, float partialTicks) {
        ItemStack helmet = state.headEquipment;
        if (helmet.isEmpty() || !(helmet.getItem() instanceof ShimmerweaveGogglesItem)) {
            return;
        }

        this.visorModel.setupAnim(state);

        collector.order(1).submitModel(
            this.visorModel, state, poseStack, VISOR_RENDER_TYPE,
            packedLight, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
    }
}
