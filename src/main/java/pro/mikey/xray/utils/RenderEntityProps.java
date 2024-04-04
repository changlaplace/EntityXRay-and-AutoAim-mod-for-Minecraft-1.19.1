package pro.mikey.xray.utils;

import com.google.common.base.Objects;
import net.minecraft.core.Position;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
//import net.minecraft.core.BlockPos;

import javax.annotation.concurrent.Immutable;

@Immutable
public class RenderEntityProps {
	public final int color;
	public final double minX;
	public final double minY;
	public final double minZ;
	public final double maxX;
	public final double maxY;
	public final double maxZ;


	public RenderEntityProps(double minX, double minY, double minZ,double maxX,double maxY,double maxZ,int color) {
		this.minX=minX;
		this.minY=minY;
		this.minZ=minZ;
		this.maxX=maxX;
		this.maxY=maxY;
		this.maxZ=maxZ;
		this.color=color;
	}

	public int getColor() {
		return color;
	}

	public Position getPos() {
		return new Position() {
			@Override
			public double x() {
				return (minX+maxX)/2;
			}

			@Override
			public double y() {
				return (minY+maxY)/2;
			}

			@Override
			public double z() {
				return (minZ+maxZ)/2;
			}
		};
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RenderEntityProps that = (RenderEntityProps) o;
		return Objects.equal(this.getPos(), that.getPos());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getPos());
	}
}
