package com.breakinblocks.auroral.integration.jade;

// TODO: Uncomment when Jade is available for 26.1

// import com.breakinblocks.auroral.Auroral;
// import com.breakinblocks.auroral.util.AuroraHelper;
// import net.minecraft.nbt.CompoundTag;
// import net.minecraft.resources.Identifier;
// import net.minecraft.server.level.ServerLevel;
// import snownee.jade.api.BlockAccessor;
// import snownee.jade.api.IServerDataProvider;
//
// public enum GlacialBasinDataProvider implements IServerDataProvider<BlockAccessor> {
//     INSTANCE;
//
//     private static final Identifier UID = Auroral.id("glacial_basin_data");
//
//     @Override
//     public void appendServerData(CompoundTag data, BlockAccessor accessor) {
//         if (accessor.getLevel() instanceof ServerLevel serverLevel) {
//             data.putBoolean("aurora_active", AuroraHelper.isAuroraActive(serverLevel));
//         }
//     }
//
//     @Override
//     public Identifier getUid() {
//         return UID;
//     }
// }
