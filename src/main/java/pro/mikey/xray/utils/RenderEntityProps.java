package pro.mikey.xray.utils;

import com.google.common.base.Objects;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.world.phys.AABB;
//import net.minecraft.core.BlockPos;

import javax.annotation.concurrent.Immutable;

@Immutable
public class RenderEntityProps {
	public final int color;
	public AABB Entity_AABB = null;

	public RenderEntityProps(AABB Entity_AABB,int color){
		this.Entity_AABB = Entity_AABB;
		this.color = color;
	}


	public RenderEntityProps(double minX, double minY, double minZ,double maxX,double maxY,double maxZ,int color) {

		this(new AABB(minX,minY,minZ,maxX,maxY,maxZ),
				color);
	}

	public int getColor() {
		return color;
	}

	public Position getPos() {
		return new Position() {
			@Override
			public double x() {
				return (Entity_AABB.minX+Entity_AABB.maxX)/2;
			}

			@Override
			public double y() {
				return (Entity_AABB.minY+Entity_AABB.maxY)/2;
			}

			@Override
			public double z() {
				return (Entity_AABB.minZ+Entity_AABB.maxZ)/2;
			}
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RenderEntityProps that = (RenderEntityProps) o;
		return Objects.equal(this.Entity_AABB, that.Entity_AABB);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getPos());
	}
}
