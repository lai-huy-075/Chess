package main.board;

import java.util.Objects;

public class Move {
	public final String move;
	public final String position;
	public final Tile source;
	public final Tile destination;

	public Move(final String move, final String position, final Tile source, final Tile destination) {
		this.move = Objects.requireNonNull(move, "Move cannot be null");
		this.position = Objects.requireNonNull(position, "Position cannot be null");
		this.source = Objects.requireNonNull(source, "Source tile cannot be null");
		this.destination = Objects.requireNonNull(destination, "Destination tile cannot be null");
	}
}