package net.grodax.f1mod.entity.custom;

import net.grodax.f1mod.block.ModBlocks;
import net.grodax.f1mod.client.KeyBindings;
import net.grodax.f1mod.entity.ModEntities;
import net.grodax.f1mod.item.ModItems;
import net.grodax.f1mod.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class F1CarEntity extends Boat {
    private static final EntityDataAccessor<Boolean> IS_DRS_ACTIVE =
            SynchedEntityData.defineId(F1CarEntity.class, EntityDataSerializers.BOOLEAN);
    // ===============================
    // CONFIG
    // ===============================
    private static final float BASE_ACCEL = 0.08f;
    private static final float MAX_SPEED = 0.6f;
    private static final float DRS_BOOST = 1.5f;
    private static final float TURN_STRENGTH = 3.5f;
    private static final float FRICTION = 0.92f;
    private static final float STRAIGHT_SPEED_FRICTION = 0.98f;

    private float steeringAngle;

    private float currentSpeed = 0f;
    private boolean drsActive = false;
    private float turnAngle = 0f;
    private boolean wasMoving = false;
    private static boolean drsPressedLastTick = false;

    private int soundCooldownTicks = 0;

    public F1CarEntity(EntityType<? extends Boat> type, Level level) {
        super(type, level);
        this.setVariant(Boat.Type.OAK);
    }

    public F1CarEntity(Level level, double x, double y, double z) {
        this(ModEntities.F1_CAR_ENTITY.get(), level);
        this.setPos(x, y, z);
    }

    // ===============================
    // STEP HEIGHT FIX (1.21+)
    // ===============================
    // Non-living entities control step height by overriding this method now.
    @Override
    public float maxUpStep() {
        return 1.25f;
    }

    // ===============================
    // DROP ITEM
    // ===============================
    @Override
    public Item getDropItem() {
        return ModItems.F1CAR.get();
    }

    // ===============================
    // MAIN TICK
    // ===============================
    @Override
    public void tick() {
        super.tick();

        if (this.isVehicle()) {
            Entity passenger = this.getFirstPassenger();

            if (passenger instanceof Player player) {
                controlCar(player);
                playEngineSound();
            }
        } else {
            // Natural slowdown - more realistic for car
            currentSpeed *= 0.85f;
        }

        if (this.isVehicle() && this.getControllingPassenger() instanceof Player player) {
            float turn = player.xxa; // A/D keys
            this.steeringAngle = turn * 30.0F; // max 30 degrees
        }
    }

    public float getSteeringAngle() {
        return this.steeringAngle;
    }

    // ===============================
    // CONTROL SYSTEM
    // ===============================
    private void controlCar(Player player) {
        float forward = player.zza;
        float strafe = player.xxa;

        // ACCELERATION - more responsive
        if (forward > 0) {
            currentSpeed += BASE_ACCEL;
        } else if (forward < 0) {
            currentSpeed -= BASE_ACCEL * 0.75f;
        } else {
            // Apply friction based on current speed and direction
            if (Math.abs(currentSpeed) > 0.01f) {
                float friction = isOnTrack() ? 0.95f : 0.92f;
                currentSpeed *= friction;
            } else {
                currentSpeed = 0f;
            }
        }

        // SPEED LIMIT
        float max = this.isDrsActive() ? 1.5F : 1.0F;

        // Track boost
        if (isOnTrack()) {
            max *= 1.25f;
        }

        currentSpeed = clamp(currentSpeed, -0.3f, max);

        // TURNING
        if (Math.abs(strafe) > 0.1f) {
            // Invert strafe for proper turning direction
            float invertedStrafe = -strafe;
            float drsTurnModifier = this.isDrsActive() ? 0.5f : 1.0f;
            // Calculate turn based on speed - faster at lower speeds
            float turnSpeed = (TURN_STRENGTH * drsTurnModifier) * (1.0f - Math.min(1.0f, Math.abs(currentSpeed) / max));
            turnAngle += invertedStrafe * turnSpeed;

            // Apply turning to rotation
            this.setYRot(this.getYRot() + turnAngle);

            // Reduce turn angle over time for smoother steering
            turnAngle *= 0.7f;
        } else {
            // Gradually reduce turn angle when not turning
            turnAngle *= 0.85f;
        }

        // MOVEMENT - Calculate flat movement (ignore looking up/down)
        Vec3 look = this.getLookAngle();
        Vec3 horizontalLook = new Vec3(look.x, 0, look.z).normalize();

        // Calculate where we want to go horizontally
        Vec3 horizontalMovement = horizontalLook.scale(currentSpeed);

        // Apply our horizontal movement, but KEEP the car's existing Y velocity (gravity/falling)
        this.setDeltaMovement(horizontalMovement.x, this.getDeltaMovement().y, horizontalMovement.z);
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    // ===============================
    // TRACK DETECTION
    // ===============================
    private boolean isOnTrack() {
        BlockPos below = this.blockPosition().below();
        return this.level().getBlockState(below).is(ModBlocks.TRACK_BLOCK.get());
    }

    // ===============================
    // DRS (toggle later via keybind)
    // ===============================
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        boolean pressed = KeyBindings.TOGGLE_DRS.isDown();

        // Trigger only on press, not hold
        if (pressed && !drsPressedLastTick) {
            if (mc.player.getVehicle() instanceof net.grodax.f1mod.entity.custom.F1CarEntity car) {
                car.toggleDRS(); // client-side toggle
            }
        }

        drsPressedLastTick = pressed;
    }

    public void toggleDRS() {
        this.drsActive = !this.drsActive;
    }

    public boolean isDrsActive() {
        return this.drsActive;
    }

    // ===============================
    // RIDING
    // ===============================
    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    protected void positionRider(Entity passenger, MoveFunction moveFunction) {
        passenger.setPos(
                this.getX(),
                this.getY(),
                this.getZ()
        );
    }

    // ===============================
    // INTERACTION
    // ===============================
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (!this.level().isClientSide) {
            player.startRiding(this);
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    // ===============================
    // SAVE DATA
    // ===============================
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        currentSpeed = tag.getFloat("Speed");
        drsActive = tag.getBoolean("DRS");
        turnAngle = tag.getFloat("TurnAngle");
        wasMoving = tag.getBoolean("WasMoving");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Speed", currentSpeed);
        tag.putBoolean("DRS", drsActive);
        tag.putFloat("TurnAngle", turnAngle);
        tag.putBoolean("WasMoving", wasMoving);
    }

    //SOUNDS
    @Override
    protected void checkInsideBlocks() { }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) { }

    @Override
    public SoundEvent getPaddleSound() {
        return null;
    }

    private void playEngineSound() {
        // Reduce the cooldown every tick until it hits 0
        if (soundCooldownTicks > 0) {
            soundCooldownTicks--;
        }

        // Only attempt to play if the car is moving and the previous sound is finished
        if (currentSpeed != 0 && soundCooldownTicks <= 0) {

            // Pitch logic: 0.5f is low/idle, 2.0f is high/screaming
            float pitch = 0.5f + (Math.min(Math.abs(currentSpeed), 1.5f) * 1.0f);
            float volume = 0.3f + (Math.min(Math.abs(currentSpeed), 0.6f) * 0.7f);

            this.level().playLocalSound(this.getX(), this.getY(), this.getZ(),
                    ModSounds.ENGINE_SOUND.get(), this.getSoundSource(),
                    volume, pitch, false);

            // Set cooldown to 60 ticks (3 seconds * 20 ticks/sec)
            soundCooldownTicks = 15;
        }
    }

    // ===============================
    // REQUIRED (1.21)
    // ===============================
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_DRS_ACTIVE, false);
    }

    // ===============================
    // UTIL
    // ===============================
    private float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    // Add this to F1CarEntity.java

    // ===============================
    // DYNAMIC HITBOX FIX
    // ===============================

    @Override
    public void setYRot(float pYRot) {
        super.setYRot(pYRot);
        // Refresh the bounding box dimensions whenever the rotation changes
        this.refreshDimensions();
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        float yaw = this.getYRot();

        // Convert degrees to radians for math
        float angleRadians = yaw * ((float)Math.PI / 180F);
        float cos = Math.abs((float)Math.cos(angleRadians));
        float sin = Math.abs((float)Math.sin(angleRadians));

        // F1 Car Dimensions: Length is 4.0, Width is 1.5
        // We calculate the "Projected" width and length
        float projectedWidth = (2.5f * sin) + (1.5f * cos);
        float projectedLength = (2.5f * cos) + (1.5f * sin);

        // Minecraft's AABB is square on the XZ plane, so we take the max
        // to ensure the car's nose or tail doesn't clip through blocks.
        float finalSize = Math.max(projectedWidth, projectedLength);

        return EntityDimensions.scalable(finalSize, 1.0f);
    }
}