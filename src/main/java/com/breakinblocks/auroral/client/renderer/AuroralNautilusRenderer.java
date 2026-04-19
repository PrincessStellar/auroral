package com.breakinblocks.auroral.client.renderer;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.client.model.AuroralNautilusArmorModel;
import com.breakinblocks.auroral.client.model.AuroralNautilusModel;
import com.breakinblocks.auroral.entity.AuroralNautilusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class AuroralNautilusRenderer extends MobRenderer<AuroralNautilusEntity, AuroralNautilusRenderState, AuroralNautilusModel> {

    private static final Identifier TEXTURE = Auroral.id("textures/entity/auroral_nautilus.png");

    public AuroralNautilusRenderer(EntityRendererProvider.Context context) {
        super(context, new AuroralNautilusModel(context.bakeLayer(AuroralNautilusModel.LAYER_LOCATION)), 0.4F);
        this.addLayer(new SimpleEquipmentLayer<>(
            this,
            context.getEquipmentRenderer(),
            EquipmentClientInfo.LayerType.NAUTILUS_BODY,
            state -> state.bodyArmorItem,
            new AuroralNautilusArmorModel(context.bakeLayer(AuroralNautilusArmorModel.LAYER_LOCATION)),
            null
        ));
    }

    @Override
    public Identifier getTextureLocation(AuroralNautilusRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public AuroralNautilusRenderState createRenderState() {
        return new AuroralNautilusRenderState();
    }

    @Override
    public void extractRenderState(AuroralNautilusEntity entity, AuroralNautilusRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.size = entity.getNautilusSize();
        renderState.bodyArmorItem = entity.getItemBySlot(EquipmentSlot.BODY).copy();
    }

    @Override
    protected void scale(AuroralNautilusRenderState renderState, PoseStack poseStack) {
        float scale = 1.0F + 0.2F * renderState.size;
        poseStack.scale(scale, scale, scale);
    }
}
