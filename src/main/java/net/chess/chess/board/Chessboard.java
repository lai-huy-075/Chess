package net.chess.chess.board;

import javafx.scene.control.ChoiceDialog;
import net.chess.chess.ChessApplication;
import net.chess.chess.file.PGNReader;
import net.chess.chess.file.PGNWriter;
import net.chess.chess.piece.*;
import net.chess.chess.player.Player;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Chess board
 *
 * @author Mr. P&#x03B9;&#x03B7;&#x03B5;&#x03B1;&#x03C1;&#x03C1;l&#x03BE;
 * @version 2022 05 23
 */
public final class Chessboard {
    /**
     * {@link String} for when the {@link PieceColor#Black} {@link Player} wins.
     */
    private static final String black_win = "0-1";

    /**
     * {@link String} for when neither {@link Player} wins and the game ends
     */
    private static final String draw = "1/2-1/2";

    /**
     * Primitive type array of {@link String} holding the options of promotion
     */
    private static final Character[] pieces = {PieceType.Queen.white, PieceType.Knight.white, PieceType.Rook.white,
            PieceType.Bishop.white};

    /**
     * {@link String} for when the {@link PieceColor#White} {@link Player} wins.
     */
    private static final String white_win = "1-0";

    /**
     * {@link Player} with the {@link PieceColor#Black} pieces
     */
    public final Player black;

    /**
     * 2d array holding the location of all the pieces
     */
    private final Tile[][] board;

    /**
     * A reference to the current {@link Player}
     */
    private Player currentPlayer;

    /**
     * Destination {@link Tile}
     */
    private Tile destination;

    /**
     * Index in position
     */
    private int index;

    /**
     * Determine if the game is over
     */
    private Mode mode;

    /**
     * {@link List} of {@link Move} made by the two {@link Player}s
     */
    private final List<Move> moves;

    /**
     * A reference to the next {@link Player}
     */
    private Player nextPlayer;

    /**
     * {@link String} holding the result
     */
    private String result;

    /**
     * Source {@link Tile}
     */
    private Tile source;

    /**
     * {@link Player} with the {@link PieceColor#White}
     */
    public final Player white;

    /**
     * Constructor
     *
     * @param mode  {@link Mode} of this
     * @param white {@link Player} controlling {@link PieceColor#White}
     * @param black {@link Player} controlling {@link PieceColor#Black}
     */
    public Chessboard(final Mode mode, final Player white, final Player black) {
        this.mode = Objects.requireNonNull(mode, "Mode cannot be null");
        this.white = Objects.requireNonNull(white, "White player cannot be null");
        this.black = Objects.requireNonNull(black, "Black player cannot be null");
        this.board = new Tile[8][8];
        this.moves = new ArrayList<>();

        this.createBoard();
        this.reset();
    }

    /**
     * Move the selected {@link Piece} from {@link #source} to {@link #destination}
     */
    private void advancePiece() {
        ChessApplication.logger.info("Advancing piece");
        final Piece src_piece = this.source.getPiece(), dest_piece = this.destination.getPiece();

        if (dest_piece != null)
            dest_piece.setTile(null);

        this.currentPlayer.incrementScore(dest_piece);
        this.destination.updatePiece(src_piece);
        this.source.reset();

        for (final Pawn pawn : this.nextPlayer.getPawn())
            pawn.setEnPassant(false);
    }

    /**
     * Append the move made to {@link #moves}
     *
     * @param attack  determine if an enemy {@link Piece} was captured
     * @param promote determine if a {@link Pawn} promoted.
     */
    private void appendMove(final boolean attack, final PromoteState promote) {
        Objects.requireNonNull(promote, "PromoteState cannot be null");
        StringBuilder move = new StringBuilder();
        final King ally_king, enemy_king;
        enemy_king = switch (this.currentPlayer.color) {
            case Black -> {
                ally_king = this.black.getKing();
                yield this.white.getKing();
            }
            case White -> {
                ally_king = this.white.getKing();
                yield this.black.getKing();
            }
            default -> throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
        };

        final CastleState castle = ally_king.getCastle();
        final CheckState check = enemy_king.getCheckState();
        final PieceType type = this.destination.getPiece().type;

        // Append Castle state
        switch (castle) {
            case Fail:
                break;
            case Kingside:
                move.append("O-O");
                break;
            case Queenside:
                move.append("O-O-O");
                break;
            case Unattempted:
                move.append(promote == PromoteState.Fail
                        ? type == PieceType.Pawn ? attack ? this.source.fileToString() : "" : String.valueOf(type.an)
                        : attack ? this.source.fileToString() : "");

                final List<Tile> valid_tiles = this.findValidTiles(type);
                switch (valid_tiles.size()) {
                    case 1:
                        break;
                    case 2:
                        final Tile other = valid_tiles.get(1);
                        if (this.source.file != other.file)
                            move.append(this.source.fileToString());
                        else if (this.source.rank != other.rank)
                            move.append(this.source.fileToString());
                        else
                            move.append(this.source.toString());
                        break;
                    default:
                        move.append(this.source.toString());
                        break;
                }

                move.append(attack ? "x" : "");
                move.append(this.destination.toString());

                break;
            default:
                throw new IllegalStateException("Illegal CastleState:\t" + ally_king.getCastle().name());
        }

        // Append promotion state
        switch (promote) {
            case Bishop:
                move.append("=B");
                break;
            case Fail:
                break;
            case Knight:
                move.append("=N");
                break;
            case Queen:
                move.append("=Q");
                break;
            case Rook:
                move.append("=R");
                break;
            default:
                throw new IllegalStateException("Illegal PromoteState:\t" + promote.name());
        }

        // Append check state
        switch (check) {
            case Check:
                move.append("+");
                break;
            case Fail, Stale:
                break;
            case Mate:
                move.append("#");
                break;
            default:
                throw new IllegalStateException("Illegal CheckState:\t" + check.name());
        }

        ChessApplication.logger.info("Appending move:\t" + move);
        this.moves.add(new Move(move.toString(), this.toString(), this.source, this.destination));
        this.index++;
        ally_king.setCastle(CastleState.Unattempted);
    }

    /**
     * Determine if a Piece can move from one {@link Tile} to another
     *
     * @param source source {@link Tile}
     * @param destination   destination {@link Tile}
     * @return {@code true}
     */
    private boolean canMove(final Tile source, final Tile destination) {
        Objects.requireNonNull(source, "Source tile cannot be null");
        Objects.requireNonNull(destination, "Destination tile cannot be null");
        final Piece piece = source.getPiece();
        if (piece == null)
            return false;
        if (piece.isAlly(destination.getPiece()))
            return false;

        final King king = switch (piece.color) {
            case Black -> this.black.getKing();
            case White -> this.white.getKing();
        };
        if (!piece.isLegal(source, destination))
            return false;
        if (this.collide(source, destination, piece.getTileTraversed(this.board, source, destination)))
            return false;
        return this.moveProtectKing(king, source, destination);
    }

    /**
     * Determine if a piece collided with any other piece when moving from one
     * {@link Tile} to another
     *
     * @param source      source {@link Tile}
     * @param destination destination {@link Tile}
     * @param traversed   List of {@link Tile}
     * @return {@code true} if a piece collides with another. false otherwise.<br>
     * {@code false} otherwise
     */
    private boolean collide(final Tile source, final Tile destination, final List<Tile> traversed) {
        Objects.requireNonNull(source, "Source tile cannot be null");
        Objects.requireNonNull(destination, "Destination tile cannot be null");
        Objects.requireNonNull(traversed, "Traversed tiles cannot be null");

        ChessApplication.logger.info("Traversed:\t" + traversed.toString());
        for (final Tile tile : traversed) {
            if (tile.equals(source) || tile.equals(destination))
                continue;

            if (tile.getPiece() != null)
                return true;
        }
        return false;
    }

    /**
     * Compares a {@link Tile} to {@link #source}
     *
     * @param tile {@link Tile} to compare with
     * @return true if {@link #source} and tile are {@link Tile#equals(Object)}<br>
     * false otherwise
     */
    public boolean compareSource(final Tile tile) {
        if (this.source == null)
            return false;
        return this.source.equals(tile);
    }

    /**
     * Initialize and add {@link Tile} to the {@link #board}.
     */
    private void createBoard() {
        ChessApplication.logger.info("Creating board");
        for (int row = 0; row < this.board.length; ++row)
            for (int col = 0; col < this.board[row].length; ++col) {
                this.board[row][col] = new Tile(row, col);
            }

        for (int row = 0; row < this.board.length; ++row)
            for (int col = 0; col < this.board[row].length; ++col) {
                this.board[row][col].setUp(row - 1 > 0 ? this.board[row - 1][col] : null);
                this.board[row][col].setDown(row + 1 < this.board.length ? this.board[row + 1][col] : null);
            }
    }

    /**
     * Draw the game
     */
    public void draw() {
        this.result = draw;
        this.endGame();
    }

    /**
     * Sets {@link #mode} to {@link Mode#Over} and calls {@link #write}
     */
    private void endGame() {
        switch (this.currentPlayer.getKing().getCheckState()) {
            case Check, Fail:
                ChessApplication.logger.info("Game ended by resignation.\n");
                break;
            case Mate:
                ChessApplication.logger.info("Game ended by checkmate.\n");
                break;
            case Stale:
                ChessApplication.logger.info("Game ended by stalemate.\n");
                break;
            default:
                throw new IllegalStateException("Illegal CheckState");
        }

        this.mode = Mode.Over;
        this.write();
    }

    /**
     * Finds a specified {@link Player}'s {@link Bishop}
     *
     * @param color {@link PieceColor} to find
     * @return location of all Bishops
     */
    private Tile[] findBishops(final PieceColor color) {
        Objects.requireNonNull(color, "PieceColor cannot be null");

        final Bishop[] p = switch (color) {
            case Black -> this.black.getBishop();
            case White -> this.white.getBishop();
            default -> throw new IllegalStateException("Illegal PieceColor:\t" + color.name());
        };
        final List<Tile> pieces = new ArrayList<>();
        for (final Bishop element : p) {
            final Tile tile = element.getTile();
            if (tile != null)
                pieces.add(tile);
        }

        return pieces.toArray(new Tile[0]);
    }

    /**
     * Finds a specified {@link Player}'s {@link Knight}
     *
     * @param color {@link PieceColor} to find
     * @return location of all Knights
     */
    private Tile[] findKnights(final PieceColor color) {
        Objects.requireNonNull(color, "PieceColor cannot be null");

        final Knight[] p = switch (color) {
            case Black -> this.black.getKnight();
            case White -> this.white.getKnight();
            default -> throw new IllegalStateException("Illegal PieceColor:\t" + color.name());
        };
        final List<Tile> pieces = new ArrayList<>();
        for (final Knight element : p) {
            final Tile tile = element.getTile();
            if (tile != null)
                pieces.add(tile);
        }

        return pieces.toArray(new Tile[0]);
    }

    /**
     * Find a piece based off a move
     *
     * @param search search query
     * @param type   {@link PieceType} to find
     * @return location of the piece
     */
    private Tile findPiece(final String search, final PieceType type) {
        if (search.length() != 2)
            throw new IllegalArgumentException("Search query must be exactly two characters long");

        final char first = search.charAt(0), second = search.charAt(1);
        final boolean bool = Character.isAlphabetic(first);
        if (bool && Character.isDigit(second))
            return this.getTile(search);
        else if (bool) {
            final int file = Character.toLowerCase(first) - 'a';
            for (final Tile[] element : this.board) {
                final Tile tile = element[file];
                final Piece piece = tile.getPiece();
                if (piece == null)
                    continue;
                if (piece.type == type)
                    return tile;
            }
        } else if (Character.isDigit(first)) {
            final int rank = first - '1';
            for (int col = 0; col < this.board.length; ++col) {
                final Tile tile = this.board[rank][col];
                final Piece piece = tile.getPiece();
                if (piece == null)
                    continue;
                if (piece.type == type)
                    return tile;
            }
        }
        throw new IllegalArgumentException("Illegal Search query:\t" + search);
    }

    /**
     * Find all pieces of a certain {@link PieceColor}
     *
     * @param color {@link PieceColor} to find
     * @return primitive type array of {@link Tile}
     */
    private Tile[] findPieces(final PieceColor color) {
        Objects.requireNonNull(color, "PieceColor cannot be null");
        ChessApplication.logger.info("Finding " + color.name() + " pieces");

        final List<Tile> pieces = new ArrayList<>();
        Tile tile;
        switch (color) {
            case White:
                for (final Piece piece : this.white.pieces) {
                    tile = piece.getTile();
                    if (tile != null)
                        pieces.add(tile);
                }
                break;
            case Black:
                for (final Piece piece : this.black.pieces) {
                    tile = piece.getTile();
                    if (tile != null)
                        pieces.add(tile);
                }
                break;
            default:
                throw new IllegalStateException("Illegal PieceColor:\t" + color.name());
        }

        return pieces.toArray(new Tile[0]);
    }

    /**
     * Finds a specified {@link Player}'s {@link Queen}
     *
     * @param color {@link PieceColor} to find
     * @return location of all Queens
     */
    private Tile[] findQueens(final PieceColor color) {
        Objects.requireNonNull(color, "PieceColor cannot be null");

        final Queen[] p = switch (color) {
            case Black -> this.black.getQueen();
            case White -> this.white.getQueen();
            default -> throw new IllegalStateException("Illegal PieceColor:\t" + color.name());
        };
        final List<Tile> pieces = new ArrayList<>();
        for (final Queen element : p) {
            final Tile tile = element.getTile();
            if (tile != null)
                pieces.add(tile);
        }

        return pieces.toArray(new Tile[0]);
    }

    /**
     * Finds a specified {@link Player}'s {@link Rook}
     *
     * @param color {@link PieceColor} to find
     * @return location of all Rooks
     */
    private Tile[] findRooks(final PieceColor color) {
        final Rook[] p = switch (color) {
            case Black -> this.black.getRook();
            case White -> this.white.getRook();
            default -> throw new IllegalStateException("Illegal PieceColor:\t" + color.name());
        };
        final List<Tile> pieces = new ArrayList<>();
        for (final Rook element : p) {
            final Tile tile = element.getTile();
            if (tile != null)
                pieces.add(tile);
        }

        return pieces.toArray(new Tile[0]);
    }

    /**
     * Find all {@link Piece} of a certain {@link PieceType} that can get to
     * {@link #destination}
     *
     * @param type      {@link PieceType} to find
     * @return locations of all valid {@link Piece}
     */
    private List<Tile> findValidTiles(final PieceType type) {
        final List<Tile> temp = new ArrayList<>();

        Tile[] tiles = switch (type) {
            case King, Pawn -> new Tile[0];
            case Queen -> this.findQueens(this.currentPlayer.color);
            case Rook -> this.findRooks(this.currentPlayer.color);
            case Knight -> this.findKnights(this.currentPlayer.color);
            case Bishop -> this.findBishops(this.currentPlayer.color);
        };

        temp.add(this.source);
        for (final Tile tile : tiles)
            if (this.canMove(tile, this.destination))
                temp.add(tile);

        return temp;
    }

    /**
     * Get {@link #board}
     *
     * @return {@link #board}
     */
    public Tile[][] getBoard() {
        return this.board;
    }

    /**
     * Get {@link #mode}
     *
     * @return {@link #mode}
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * Get {@link #moves}
     *
     * @return {@link #moves}
     */
    public List<Move> getMoves() {
        return this.moves;
    }

    /**
     * Get {@link #nextPlayer}
     *
     * @return {@link #nextPlayer}
     */
    public Player getNextPlayer() {
        return this.nextPlayer;
    }

    /**
     * Get {@link Tile} in a position specified by a {@link String}
     *
     * @param tile {@link String} representation of the {@link Tile}
     * @return {@link Tile} from {@link #board}
     */
    private Tile getTile(final String tile) throws ArrayIndexOutOfBoundsException {
        if (tile == null)
            return null;
        final int col = tile.charAt(0) - 'a';
        final int row = '8' - tile.charAt(1);
        return this.board[row][col];
    }

    /**
     * Get a {@link Tile} that is offset from an inputted Tile
     *
     * @param tile {@link Tile} to serve as the origin
     * @param x    horizontal offset
     * @param y    vertical offset
     * @return Tile offset
     * @throws ArrayIndexOutOfBoundsException when offset goes out of bounds
     */
    public Tile getTileOffset(final Tile tile, final int x, final int y) throws ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(tile, "Origin tile cannot be null");
        return this.board[tile.rank + y][tile.file + x];
    }

    /**
     * Determine if {@link #mode} is {@link Mode#Over}
     *
     * @return true if the game is over<br>
     * false if the game is not over
     */
    public boolean isGameOver() {
        return Objects.requireNonNull(this.mode) == Mode.Over;
    }

    /**
     * Handle the {@link King} castling
     *
     * @return {@link CastleState} depending upon if the King castled.
     */
    private CastleState kingCastled() {
        if (!(this.source.getPiece() instanceof King king))
            return CastleState.Unattempted;

        if (this.destination.rank != (king.color == PieceColor.White ? 7 : 0))
            return CastleState.Unattempted;

        Piece piece;
        switch (this.destination.file) {
            case 2:
                if (!king.canQueensideCastle())
                    return CastleState.Fail;

                for (int i = 1; i < this.source.file; ++i) {
                    if (this.board[this.source.rank][i].getPiece() != null)
                        return CastleState.Fail;
                    if (this.kingMoveIntoCheck(this.board[this.source.rank][i]))
                        return CastleState.Fail;
                }

                piece = this.board[this.source.rank][0].getPiece();

                this.board[this.source.rank][0].reset();
                this.board[this.source.rank][3].updatePiece(piece);
                king.setCastle(CastleState.Queenside);
                return CastleState.Queenside;
            case 6:
                if (!king.canKingsideCastle())
                    return CastleState.Fail;

                for (int i = this.source.file + 1; i < 7; ++i) {
                    if (this.board[this.source.rank][i].getPiece() != null)
                        return CastleState.Fail;
                    if (this.kingMoveIntoCheck(this.board[this.source.rank][i]))
                        return CastleState.Fail;
                }

                piece = this.board[this.source.rank][7].getPiece();
                this.board[this.source.rank][7].reset();
                this.board[this.source.rank][5].updatePiece(piece);
                king.setCastle(CastleState.Kingside);
                return CastleState.Kingside;
            default:
                return CastleState.Unattempted;
        }
    }

    /**
     * Determine if the {@link King} moves itself into a Check
     *
     * @param tile {@link Tile} to check
     * @return true if the {@link King} moves itself into a check<br>
     * false otherwise
     */
    private boolean kingMoveIntoCheck(final Tile tile) {
        Objects.requireNonNull(tile, "Tile cannot be null");
        if (!(this.source.getPiece() instanceof King))
            return false;

        final Piece sp = this.source.getPiece(), dp = this.destination.getPiece();
        this.source.reset();
        this.destination.updatePiece(sp);

        for (final Tile enemy : this.findPieces(sp.color.opponent())) {
            final Piece piece = enemy.getPiece();
            if (!piece.isLegal(enemy, tile))
                continue;

            if (this.collide(enemy, tile, piece.getTileTraversed(this.board, enemy, tile)))
                continue;

            this.source.updatePiece(sp);
            this.destination.updatePiece(dp);
            return true;
        }

        this.source.updatePiece(sp);
        this.destination.updatePiece(dp);
        return false;
    }

    /**
     * Load the initial position of the {@link Chessboard}
     */
    public void loadInitialPosition() {
        if (this.moves.isEmpty())
            return;

        this.index = 0;
        this.loadPosition();
    }

    /**
     * Load last position in {@link Move#position()}
     */
    public void loadLastPosition() {
        if (this.moves.isEmpty())
            return;
        this.index = this.moves.size() - 1;
        this.loadPosition();
    }

    /**
     * Load moves from a pre-set list of moves.
     *
     * @param moves variable argument of moves to load in
     * @throws ParseException if loading moves fails.
     */
    public void loadMoves(final String... moves) throws ParseException {
        Objects.requireNonNull(moves, "Moves cannot be null");
        this.reset();
        for (final String move : moves) {
            if (move == null)
                throw new ParseException("Null Move", 0);

            ChessApplication.logger.info("Parsing move:\t" + move);
            PromoteState promote = PromoteState.Fail;
            final Tile tile = this.getTile(PGNReader.getLast(move, "[a-h][1-8]"));
            this.destination = tile;
            String m;
            try {
                m = move.substring(1, 3);
            } catch (final StringIndexOutOfBoundsException sioobe) {
                m = null;
            }

            Tile src;
            try {
                src = this.getTile(m);
            } catch (final ArrayIndexOutOfBoundsException aioobe) {
                src = null;
            }
            if (this.destination != null && this.destination.equals(src))
                src = null;

            switch (move.charAt(0)) {
                case 'K':
                    switch (this.currentPlayer.color) {
                        case Black:
                            this.source = this.black.getKing().getTile();
                            break;
                        case White:
                            this.source = this.white.getKing().getTile();
                            break;
                        default:
                            throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
                    }
                    break;
                case 'Q':
                    if (src == null)
                        for (final Tile queen : this.findQueens(this.currentPlayer.color)) {
                            if (!this.canMove(queen, tile))
                                continue;
                            this.source = queen;
                            break;
                        }
                    else
                        this.source = this.findPiece(m, PieceType.Queen);
                    break;
                case 'R':
                    if (src == null)
                        for (final Tile rook : this.findRooks(this.currentPlayer.color)) {
                            if (!this.canMove(rook, tile))
                                continue;
                            this.source = rook;
                            break;
                        }
                    else
                        this.source = this.findPiece(m, PieceType.Rook);
                    break;
                case 'N':
                    if (src == null)
                        for (final Tile knight : this.findKnights(this.currentPlayer.color)) {
                            if (!this.canMove(knight, tile))
                                continue;
                            this.source = knight;
                            break;
                        }
                    else
                        this.source = this.findPiece(m, PieceType.Knight);
                    break;
                case 'B':
                    if (src == null)
                        for (final Tile bishop : this.findBishops(this.currentPlayer.color)) {
                            if (!this.canMove(bishop, tile))
                                continue;
                            this.source = bishop;
                            break;
                        }
                    else
                        this.source = this.findPiece(m, PieceType.Bishop);
                    break;
                default:
                    if (tile == null) {
                        final int castle = move.length() - move.replace("-", "").length();
                        switch (castle) {
                            case 1:
                                switch (this.currentPlayer.color) {
                                    case Black:
                                        this.source = this.black.getKing().getTile();
                                        this.destination = this.board[0][6];
                                        break;
                                    case White:
                                        this.source = this.white.getKing().getTile();
                                        this.destination = this.board[7][6];
                                        break;
                                    default:
                                        throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
                                }
                                break;
                            case 2:
                                switch (this.currentPlayer.color) {
                                    case Black:
                                        this.source = this.black.getKing().getTile();
                                        this.destination = this.board[0][2];
                                        break;
                                    case White:
                                        this.source = this.white.getKing().getTile();
                                        this.destination = this.board[7][2];
                                        break;
                                    default:
                                        throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
                                }
                                break;
                            default:
                                throw new ParseException("Unkown move:\t" + move, 0);
                        }
                    } else {
                        final int dx = switch (this.currentPlayer.color) {
                            case Black -> -1;
                            case White -> 1;
                            default ->
                                    throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
                        };
                        promote = PGNReader.extractPromote(move);
                        if (move.charAt(1) == 'x')
                            switch (move.charAt(2) - move.charAt(0)) {
                                case -1:
                                    this.source = this.board[tile.rank + dx][tile.file + 1];
                                    break;
                                case 1:
                                    this.source = this.board[tile.rank + dx][tile.file - 1];
                                    break;
                                default:
                                    throw new ParseException("Illegal Capture by the Pawn:\t" + move, 0);
                            }
                        else
                            for (int i = 1; i < 3; ++i) {
                                final Piece piece = this.board[tile.rank + dx * i][tile.file].getPiece();
                                if (piece instanceof Pawn) {
                                    this.source = this.board[tile.rank + dx * i][tile.file];
                                    break;
                                }
                            }
                    }
                    break;
            }
            this.movePiece(promote);
        }

        switch (this.result.length()) {
            case 3:
                this.resign();
                break;
            case 7:
                this.draw();
                break;
            default:
                throw new IllegalStateException("Illegal result:\t" + this.result);
        }
    }

    /**
     * Increment {@link #index} and load the position in {@link Move#position()}
     */
    public void loadNextPosition() {
        if (this.index + 1 >= this.moves.size())
            return;
        ++this.index;
        this.loadPosition();
    }

    /**
     * Load position from {@link Move#position()} at {@link #index}
     */
    private void loadPosition() {
        ChessApplication.logger.info("Loading position:\t" + this.index + "\n");
        final String position = this.moves.get(this.index).position();
        String piece;
        int col = 0, row = 0;
        for (final String line : position.split("/"))
            for (final char c : line.toCharArray()) {
                switch (c) {
                    case 'k':
                        piece = String.valueOf(PieceType.King.black);
                        break;
                    case 'K':
                        piece = String.valueOf(PieceType.King.white);
                        break;
                    case 'q':
                        piece = String.valueOf(PieceType.Queen.black);
                        break;
                    case 'Q':
                        piece = String.valueOf(PieceType.Queen.white);
                        break;
                    case 'r':
                        piece = String.valueOf(PieceType.Rook.black);
                        break;
                    case 'R':
                        piece = String.valueOf(PieceType.Rook.white);
                        break;
                    case 'n':
                        piece = String.valueOf(PieceType.Knight.black);
                        break;
                    case 'N':
                        piece = String.valueOf(PieceType.Knight.white);
                        break;
                    case 'b':
                        piece = String.valueOf(PieceType.Bishop.black);
                        break;
                    case 'B':
                        piece = String.valueOf(PieceType.Bishop.white);
                        break;
                    case 'p':
                        piece = String.valueOf(PieceType.Pawn.black);
                        break;
                    case 'P':
                        piece = String.valueOf(PieceType.Pawn.white);
                        break;
                    default:
                        piece = "";
                        for (int i = '0'; i < c; ++i) {
                            this.board[row][col].setTextFill(null);
                            this.board[row][col].setText(piece);
                            if (++col == 8) {
                                ++row;
                                col = 0;
                            }
                        }
                        continue;
                }

                this.board[row][col]
                        .setTextFill(Character.isLowerCase(c) ? PieceColor.Black.color : PieceColor.White.color);
                this.board[row][col].setText(piece);
                if (++col == 8) {
                    ++row;
                    col = 0;
                }
            }
    }

    /**
     * Decrement {@link #index} and load the position in {@link Move#position()}
     */
    public void loadPreviousPosition() {
        if (this.index - 1 < 0)
            return;

        --this.index;
        this.loadPosition();
    }

    /**
     * Handling logic of moving a piece.<br>
     * Actual updating of the GUI is done in {@link #advancePiece()}
     */
    private void movePiece(PromoteState promote) {
        final Piece src_piece = this.source.getPiece();
        if (src_piece == null)
            return;

        ChessApplication.logger.info(String.format("Moving %s from %s to %s", src_piece.toFigure(), this.source.toString(),
                this.destination.toString()));

        if (this.source.equals(this.destination)) {
            ChessApplication.logger.info("No movement detected.");
            return;
        }

        final boolean isAlly = this.source.getPiece().isAlly(this.destination.getPiece());
        ChessApplication.logger.info(isAlly ? "Attempting to capture Ally" : "Capturing enemy or moving to empty tile");
        if (isAlly)
            return;

        final boolean legal = src_piece.isLegal(this.source, this.destination);
        ChessApplication.logger.info(legal ? "Move is legal" : "Move is not legal");
        if (!legal)
            return;

        final boolean collide = this.collide(this.source, this.destination,
                src_piece.getTileTraversed(this.board, this.source, this.destination));
        ChessApplication.logger.info(
                src_piece.toFigure() + (collide ? " collided on its journey" : " did not collide on its journey"));
        if (collide)
            return;

        King ally_king, enemy_king;
        enemy_king = switch (this.currentPlayer.color) {
            case Black -> {
                ally_king = this.black.getKing();
                yield this.white.getKing();
            }
            case White -> {
                ally_king = this.white.getKing();
                yield this.black.getKing();
            }
            default -> throw new IllegalStateException("Illegal PieceColor:\t" + this.currentPlayer.color.name());
        };

        if (src_piece instanceof King) {
            final boolean kingMoveIntoCheck = this.kingMoveIntoCheck(this.destination);
            ChessApplication.logger.info(kingMoveIntoCheck ? "King moved into Check" : "King did not move into Check");
            if (kingMoveIntoCheck)
                return;

            final CastleState castle = this.kingCastled();
            ChessApplication.logger.info("CastleState:\t" + castle.name());
            switch (castle) {
                case Fail:
                    return;
                case Kingside:
                    break;
                case Queenside:
                    break;
                case Unattempted:
                    break;
                default:
                    throw new IllegalStateException("Illegal CastleState:\t" + castle.name());
            }
        } else {
            final boolean protect = this.moveProtectKing(ally_king, this.source, this.destination);
            ChessApplication.logger.info(protect ? "Move protects the King" : "Move does not protect the King");
            if (!protect)
                return;
        }

        boolean attack = this.destination.getPiece() != null;
        if (src_piece instanceof Pawn) {
            final boolean diagonal = ((Pawn) src_piece).getDiagonal();
            switch (src_piece.color) {
                case Black:
                    if (diagonal && !attack) {
                        attack = this.destination.getUp().getPiece() != null;
                        this.destination.getUp().reset();
                    }

                    if (this.destination.rank == 7)
                        promote = this.mode == Mode.Debug ? this.promote(promote) : this.promote();
                    break;
                case White:
                    if (diagonal && !attack) {
                        attack = this.destination.getDown().getPiece() != null;
                        this.destination.getDown().reset();
                    }

                    if (this.destination.rank == 0)
                        promote = this.mode == Mode.Debug ? this.promote(promote) : this.promote();
                    break;
                default:
                    break;
            }
        }

        this.advancePiece();

        this.updateCastle(ally_king);

        this.updateCheck(enemy_king);
        this.updateCheckMate(enemy_king);
        this.updateStalemate(enemy_king);

        this.appendMove(attack, promote);
        this.updatePlayers();

        switch (enemy_king.getCheckState()) {
            case Mate:
                switch (this.currentPlayer.color) {
                    case Black:
                        this.result = "1-0";
                        break;
                    case White:
                        this.result = "0-1";
                        break;
                    default:
                        throw new IllegalStateException(
                                "Illegal current Player PieceColor:\t" + this.currentPlayer.color.name());
                }

                this.endGame();
                break;
            case Stale:
                this.draw();
                break;
            default:
                break;
        }
    }

    /**
     * Determine if a move will protect the {@link King} from check.<br>
     * This method assumes that movement from source to destination is
     * {@link Piece#isLegal(Tile, Tile)} and does not
     * {@link #collide(Tile, Tile, List)}
     *
     * @param king        The {@link King} that needs protection
     * @param source      The {@link Tile} a piece is moving from
     * @param destination The {@link Tile} a piece is moving to
     * @return {@code true} if the move protects the {@link King}<br>
     * {@code false} otherwise
     */
    private boolean moveProtectKing(final King king, final Tile source, final Tile destination) {
        Objects.requireNonNull(king, "King cannot be null");
        Objects.requireNonNull(source, "Source tile cannot be null");
        Objects.requireNonNull(destination, "Destination tile cannot be null");

        final Piece sp = source.getPiece(), dp = destination.getPiece();
        source.reset();
        destination.updatePiece(sp);
        if (dp != null)
            dp.setTile(null);

        final Tile king_tile = sp instanceof King ? destination : king.getTile();

        for (final Tile enemy : this.findPieces(king.color.opponent())) {
            final Piece piece = enemy.getPiece();

            if (!piece.isLegal(enemy, king_tile)
                    || this.collide(enemy, king_tile, piece.getTileTraversed(this.board, enemy, king_tile)))
                continue;

            source.updatePiece(sp);
            destination.updatePiece(dp);
            if (dp != null)
                dp.setTile(destination);
            return false;
        }

        source.updatePiece(sp);
        destination.updatePiece(dp);
        if (dp != null)
            dp.setTile(destination);
        return true;
    }

    /**
     * Place the {@link Piece} on {@link #board}
     */
    private void placePieces() {
        ChessApplication.logger.info("Placing pieces on the Chess board");

        int indexInList = 0;
        Tile tile;
        Piece piece;

        // Place white pieces
        for (int i = 6; i < 8; ++i)
            for (int j = 0; j < 8; ++j) {
                tile = this.board[i][j];
                piece = this.white.pieces[indexInList];
                piece.reset();
                tile.updatePiece(piece);
                piece.setTile(tile);
                ++indexInList;
            }

        // Place black pieces
        indexInList = 0;
        for (int i = 1; i > -1; --i)
            for (int j = 0; j < 8; ++j) {
                tile = this.board[i][j];
                piece = this.black.pieces[indexInList];
                piece.reset();
                tile.updatePiece(piece);
                piece.setTile(tile);
                ++indexInList;
            }
    }

    /**
     * Promote a pawn
     *
     * @return {@link PromoteState}
     */
    private PromoteState promote() {
        ChoiceDialog<Character> dialog = new ChoiceDialog<>(pieces[0], pieces);
        dialog.setTitle("Promote");
        dialog.setContentText("Pick a piece");
        final Optional<Character> result = dialog.showAndWait();

        if (result.isEmpty())
            throw new IllegalStateException("Promote dialog failed");

        final char piece = result.get();

        final PromoteState state = switch (piece) {
            case '♘' -> PromoteState.Knight;
            case '♖' -> PromoteState.Rook;
            case '♗' -> PromoteState.Bishop;
            default -> PromoteState.Queen;
        };
        ChessApplication.logger.info("Promoted Pawn to " + state.name());
        return this.promote(state);
    }

    private PromoteState promote(final PromoteState state) {
        final int file = ((Pawn) this.source.getPiece()).starting_File;
        switch (state) {
            case Queen:
                this.currentPlayer.pieces[file] = new Queen(this.currentPlayer.color);
                break;
            case Knight:
                this.currentPlayer.pieces[file] = new Knight(this.currentPlayer.color);
                break;
            case Rook:
                this.currentPlayer.pieces[file] = new Rook(this.currentPlayer.color);
                break;
            case Bishop:
                this.currentPlayer.pieces[file] = new Bishop(this.currentPlayer.color);
                break;
            case Fail:
                break;
            default:
                throw new IllegalStateException("Illegal PromoteState:\t" + state.name());
        }

        this.source.updatePiece(this.currentPlayer.pieces[file]);
        return state;
    }

    /**
     * Terminates the program.<br>
     * Does not edit any files
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Reset the board and all attributes.
     */
    public void reset() {
        this.resetBoard();
        this.resetTiles();
        this.placePieces();
        this.white.reset();
        this.black.reset();
        this.moves.clear();
        this.index = 0;
        this.currentPlayer = this.white;
        this.nextPlayer = this.black;
        ChessApplication.logger.info("Reset Chess board\n\n");
    }

    /**
     * Reset {@link #board}
     */
    private void resetBoard() {
        ChessApplication.logger.info("Reset board array");
        for (final Tile[] row : this.board)
            for (final Tile tile : row)
                tile.reset();
    }

    /**
     * Set {@link #source} and {@link #destination} to null
     */
    public void resetTiles() {
        ChessApplication.logger.info("Resetting Tiles\n");
        this.source = null;
        this.destination = null;
    }

    /**
     * {@link #currentPlayer} resigns
     */
    public void resign() {
        switch (this.currentPlayer.color) {
            case Black:
                this.result = white_win;
                break;
            case White:
                this.result = black_win;
                break;
            default:
                throw new IllegalStateException("Illegal current Player PieceColor:\t" + this.currentPlayer.color.name());
        }
        this.endGame();
    }

    /**
     * Determine the result of the game
     *
     * @return {@link #result}
     */
    public String result() {
        return this.result;
    }

    /**
     * Set {@link #result}
     *
     * @param result new {@link #result}
     */
    public void setResult(final String result) {
        this.result = result;
    }

    /**
     * This method is called whenever a {@link Tile} is clicked.
     */
    public void tileClicked() {
        switch (this.mode) {
            case Debug, Over:
                return;
            case Funny, Normal:
                break;
            default:
                throw new IllegalStateException("Illegal Mode:\t" + this.mode);
        }

        if (this.source == null)
            return;

        if (!this.currentPlayer.movingAlly(this.source)) {
            this.resetTiles();
            return;
        }

        if (this.destination == null)
            return;

        this.movePiece(PromoteState.Fail);
        this.resetTiles();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        int count;
        for (final Tile[] row : this.board) {
            count = 0;
            for (final Tile tile : row) {
                final Piece piece = tile.getPiece();
                if (piece == null)
                    ++count;
                else {
                    if (count != 0)
                        out.append(count);
                    out.append(piece.toFEN());
                    count = 0;
                }
            }
            if (count != 0)
                out.append(count);
            out.append("/");
        }

        return out.substring(0, out.length() - 1);
    }

    /**
     * Check for ally rooks and update King's castle status
     *
     * @param king {@link King} itself
     */
    private void updateCastle(final King king) {
        Objects.requireNonNull(king, "King cannot be null");
        ChessApplication.logger.info(String.format("Updating %s King.king and King.queen", king.color.name()));
        final Piece piece = this.destination.getPiece();

        if (piece instanceof King) {
            king.setKingside(false);
            king.setQueenside(false);
            return;
        }

        if (piece instanceof Rook)
            switch (this.source.file) {
                case 0:
                    king.setQueenside(false);
                    return;
                case 7:
                    king.setKingside(false);
                    return;
                default:
                    return;
            }
    }

    /**
     * Update King's check state to {@link CheckState#Check} if required
     *
     * @param king {@link King}
     */
    private void updateCheck(final King king) {
        Objects.requireNonNull(king, "King cannot be null");
        ChessApplication.logger.info(String.format("Updating %s King.check", king.color.name()));

        final Tile king_tile = king.getTile();

        for (final Tile enemy : this.findPieces(king.color.opponent())) {
            final Piece piece = enemy.getPiece();

            if (!piece.isLegal(enemy, king_tile))
                continue;

            if (this.collide(enemy, king_tile, piece.getTileTraversed(this.board, enemy, king_tile)))
                continue;

            king.setCheck(CheckState.Check);
            return;
        }

        king.setCheck(CheckState.Fail);
    }

    /**
     * Update King's check state to {@link CheckState#Mate} if required
     *
     * @param king {@link King}
     */
    private void updateCheckMate(final King king) {
        Objects.requireNonNull(king, "King cannot be null");
        ChessApplication.logger.info("Updating CheckState for " + king.color + " King to CheckState.Mate");
        if (king.getCheckState() == CheckState.Fail)
            return;

        // Find all attacking pieces and their path to the King
        final Tile king_tile = king.getTile();
        List<Tile> traverse = new ArrayList<>();
        for (final Tile enemy : this.findPieces(king.color.opponent())) {
            final Piece piece = enemy.getPiece();
            if (!piece.isLegal(enemy, king_tile))
                continue;

            List<Tile> traversed = piece.getTileTraversed(this.board, enemy, king_tile);
            if (this.collide(enemy, king_tile, traversed))
                continue;

            traverse.addAll(traversed);
        }

        // Determine if any ally piece can protect the king
        for (final Tile ally : this.findPieces(king.color)) {
            final Piece piece = ally.getPiece();
            for (final Tile protector: traverse) {
                if (!this.canMove(ally, protector))
                    continue;

                ChessApplication.logger.info("Found defending piece:\t" + piece.toFigure());
                ChessApplication.logger.info(String.format("Move from %s to %s", ally, protector));
                return;
            }
        }

        // Determine if the King can move itself out of check
        for (final Tile tile : king.getSurround(this.board)) {
            if (king.isAlly(tile.getPiece()))
                continue;

            if (this.moveProtectKing(king, king_tile, tile))
                return;
        }

        king.setCheck(CheckState.Mate);
    }

    /**
     * Update {@link #destination}
     *
     * @param tile new destination {@link Tile}
     */
    public void updateDestination(final Tile tile) {
        Objects.requireNonNull(tile, "New destination tile cannot be null");
        ChessApplication.logger.info("Updating destination:\t" + tile + "\n\n");

        if (this.destination == null)
            this.destination = tile;
    }

    /**
     * Swap {@link #currentPlayer} and {@link #nextPlayer}
     */
    private void updatePlayers() {
        final Player temp = this.currentPlayer;
        this.currentPlayer = this.nextPlayer;
        this.nextPlayer = temp;
    }

    /**
     * Update {@link #source}
     *
     * @param tile new source {@link Tile}
     */
    public void updateSource(final Tile tile) {
        Objects.requireNonNull(tile, "New source tile cannot be null");
        ChessApplication.logger.info("Updating source:\t" + tile.toString());

        if (this.source == null)
            this.source = tile;
    }

    /**
     * Update King's check state to {@link CheckState#Stale} if necessary.
     *
     * @param king {@link King} to update.
     */
    public void updateStalemate(final King king) {
        ChessApplication.logger.info("Updating " + king.color.name() + " King.check to CheckState.Stale");
        if (king.getCheckState() != CheckState.Fail)
            return;

        final Tile[] pieces = this.findPieces(king.color);
        for (final Tile[] row : this.board) {
            for (final Tile tile : row) {
                for (Tile piece : pieces) {
                    if (this.canMove(piece, tile)) {
                        ChessApplication.logger.info("Move from " + piece + " to " + tile);
                        return;
                    }
                }
            }
        }

        king.setCheck(CheckState.Stale);
    }

    /**
     * Write {@link ChessApplication#pgn_file} with details of the game
     */
    public void write() {
        PGNWriter.write(this);
    }
}
