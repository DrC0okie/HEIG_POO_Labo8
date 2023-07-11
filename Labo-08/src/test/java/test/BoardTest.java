package test;

import chess.PlayerColor;
import engine.board.Board;
import engine.board.Cell;
import engine.listeners.EngineObserver;
import engine.pieces.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest implements EngineObserver {
    PlayerColor white = PlayerColor.WHITE;
    PlayerColor black = PlayerColor.BLACK;

    /**
     * Function that create a custom board depending on the test
     *
     * @param board  the board to fill
     * @param pieces the pieces to add
     */
    public void createCustomBoard(Board board, Piece... pieces) {
        board.init();
        board.clear();

        for (Piece piece : pieces)
            board.addPiece(piece);
    }
    
    @Test
    @DisplayName("test if the board correctly moves a piece")
    public void testMove() {
        Board board = new Board(this);
        Cell from = new Cell(1, 1);
        Cell to = new Cell(2, 2);
        Queen queen = new Queen(white, from);
        createCustomBoard(board, queen);

        board.move(queen.getCell().getX(), queen.getCell().getY(), to.getX(), to.getY(), white);
        assertNotEquals(queen.getCell(), from, "the piece should have moved");
        assertEquals(queen.getCell(), to, "the piece should have moved to the new cell");
    }

    @Test
    @DisplayName("test if addPiece() adds a piece to the board")
    public void testAddPiece() {
        Board board = new Board(this);
        Pawn pawn = new Pawn(white, new Cell(0, 0), board);
        createCustomBoard(board, pawn);
        assertEquals(board.getPieces().get(new Cell(0, 0)), pawn, "the piece should have been added to the board");
    }

    @Test
    @DisplayName("test if removePiece() removes a piece from the board")
    public void testRemovePiece() {
        Board board = new Board(this);
        Pawn pawn = new Pawn(white, new Cell(0, 0), board);
        createCustomBoard(board, pawn);
        board.removePiece(pawn.getCell());
        assertNull(board.getPieces().get(new Cell(0, 0)), "the piece should have been removed from the board");
    }


    @Test
    @DisplayName("test if clear() removes all pieces from the board")
    public void testClear() {
        Board board = new Board(this);
        Piece piece1 = new Rook(white, new Cell(0, 0));
        Piece piece2 = new Rook(white, new Cell(1, 1));
        createCustomBoard(board, piece1, piece2);

        //check if the pieces are on the board
        assertEquals(board.getPieces().get(new Cell(0, 0)), piece1, "the piece should have been added to the board");
        assertEquals(board.getPieces().get(new Cell(1, 1)), piece2, "the piece should have been added to the board");

        board.clear();
        //check if the pieces are removed from the board
        assertNull(board.getPieces().get(new Cell(0, 0)), "the piece should have been removed from the board");
        assertNull(board.getPieces().get(new Cell(1, 1)), "the piece should have been removed from the board");
    }

    @Test
    @DisplayName("test isCheck() returns true if the king is in check")
    public void testIsCheck() {
        Board board = new Board(this);
        King king = new King(white, new Cell(0, 0), board.getPieces(), board);
        Queen queen = new Queen(PlayerColor.BLACK, new Cell(1, 1));
        createCustomBoard(board, king, queen);

        assertTrue(king.isCheck(), "the king should be in check");
    }

    @Test
    @DisplayName("test isCheck() returns false if the king is not in check")
    public void testIsCheckFalse() {
        Board board = new Board(this);
        King king = new King(white, new Cell(0, 0), board.getPieces(), board);
        Queen queen = new Queen(PlayerColor.BLACK, new Cell(1, 2));
        createCustomBoard(board, king, queen);

        assertFalse(king.isCheck(), "the king should not be in check");
    }


    @Test
    @DisplayName("test if a piece checks only a piece of the opposite color")
    public void testIsCheckOnlyOppositeColor() {
        Board board = new Board(this);
        Cell from = new Cell(1, 1);
        Cell checkable = new Cell(2, 2);
        Bishop whiteBishop = new Bishop(black, from);
        Bishop blackBishop = new Bishop(white, checkable);
        createCustomBoard(board, whiteBishop, blackBishop);

        //checks if the bishop can move to the cell
        assertTrue(board.move(whiteBishop.getCell().getX(), whiteBishop.getCell().getY(), checkable.getX(), checkable.getY(), whiteBishop.getColor()), "the bishop should be able to move to the cell because the cell is occupied by a piece of the opposite color");

        Cell notCheckable = new Cell(3, 3);
        Bishop whiteBishop2 = new Bishop(white, from);
        Bishop notCheckableBishop = new Bishop(white, notCheckable);
        createCustomBoard(board, whiteBishop2, notCheckableBishop);

        //checks if the bishop cannot move to the cell
        assertFalse(board.move(whiteBishop2.getCell().getX(), whiteBishop2.getCell().getY(), notCheckable.getX(), notCheckable.getY(), whiteBishop2.getColor()), "the bishop should not be able to move to the cell because the cell is occupied by a piece of the same color");

    }

    //Bishop tests


    @Test
    @DisplayName("test if the bishop correctly moves")
    public void testBishopLegalMove() {
        Board board = new Board(this);
        Cell from = new Cell(1, 1);
        Cell to = new Cell(2, 2);
        Bishop whiteBishop = new Bishop(white, new Cell(1, 1));

        createCustomBoard(board, whiteBishop);
        assertTrue(board.move(whiteBishop.getCell().getX(), whiteBishop.getCell().getY(), to.getX(), to.getY(), whiteBishop.getColor()), "the bishop should be able to move diagonally");

        Cell from2 = new Cell(3, 3);
        Cell to2 = new Cell(0, 6);
        Bishop whiteBishop2 = new Bishop(white, from2);

        createCustomBoard(board, whiteBishop2);
        assertTrue(board.move(whiteBishop2.getCell().getX(), whiteBishop2.getCell().getY(), to2.getX(), to2.getY(), whiteBishop2.getColor()), "the bishop should be able to move diagonally");

    }

    @Test
    @DisplayName("test if the bishop cannot move horizontally or vertically")
    public void testBishopIllegalMove() {
        Board board = new Board(this);
        Bishop whiteBishop = new Bishop(white, new Cell(1, 1));
        createCustomBoard(board, whiteBishop);

        //white bishop
        //horizontal move
        assertFalse(board.move(whiteBishop.getCell().getX(), whiteBishop.getCell().getY(), 2, 1, whiteBishop.getColor()), "the bishop should not be able to move horizontally");
        //vertical move
        assertFalse(board.move(whiteBishop.getCell().getX(), whiteBishop.getCell().getY(), 1, 2, whiteBishop.getColor()), "the bishop should not be able to move vertically");

    }

    @Test
    @DisplayName("test pieces between moves")
    public void testBishopPiecesBetweenMoves() {
        Board board = new Board(this);
        Cell from = new Cell(1, 1);
        Cell between = new Cell(2, 2);
        Cell to = new Cell(3, 3);
        Bishop whiteBishop = new Bishop(white, from);
        Bishop blackBishop = new Bishop(black, between);

        createCustomBoard(board, whiteBishop, blackBishop);

        // Test that white bishop cannot move over black bishop
        assertFalse(board.move(whiteBishop.getCell().getX(), whiteBishop.getCell().getY(), to.getX(), to.getY(), whiteBishop.getColor()), "white bishop cannot move because there is a piece between the two cells")
        ;
    }

    @Test
    @DisplayName("test if the knight correctly moves")
    public void testKnightLegalMove() {
        Board board = new Board(this);
        Knight whiteKnight = new Knight(white, new Cell(2, 7));
        Knight blackKnight = new Knight(black, new Cell(1, 1));
        createCustomBoard(board, whiteKnight, blackKnight);

        // Test valid L-shaped move for knight
        //white knight
        assertTrue(board.move(whiteKnight.getCell().getX(), whiteKnight.getCell().getY(), 4, 6, whiteKnight.getColor()), "The white Knight did a legal move");
        //black knight
        assertTrue(board.move(blackKnight.getCell().getX(), blackKnight.getCell().getY(), 0, 3, blackKnight.getColor()), "The black Knight did a legal move");
    }

    @Test
    @DisplayName("test if the knight did an illegal move")
    public void testKnightIllegalMove() {
        Board board = new Board(this);
        Knight blackKnight = new Knight(black, new Cell(1, 1));

        //horizontal move
        assertFalse(board.move(blackKnight.getCell().getX(), blackKnight.getCell().getY(), 2, 1, blackKnight.getColor()), "The black Knight did an illegal move");
        //vertical move
        assertFalse(board.move(blackKnight.getCell().getX(), blackKnight.getCell().getY(), 1, 2, blackKnight.getColor()), "The black Knight did an illegal move");
        //diagonal move
        assertFalse(board.move(blackKnight.getCell().getX(), blackKnight.getCell().getY(), 2, 2, blackKnight.getColor()), "The black Knight did an illegal move");
        //move of three cases
        assertFalse(board.move(blackKnight.getCell().getX(), blackKnight.getCell().getY(), 4, 1, blackKnight.getColor()), "The black Knight did an illegal move");
    }

    //Castling tests
    @Test
    @DisplayName("test if the king can castle")
    public void testValidCastling() {
        Board board = new Board(this);
        King king = new King(white, new Cell(4, 0), board.getPieces(), board);
        Rook rook = new Rook(white, new Cell(7, 0));
        Cell to = new Cell(6, 0);
        createCustomBoard(board, king, rook);

        //use castling method instead of move because the king is moved by updateCastling

        // Test that white king can castle (castling on the right side)
        assertTrue(king.castling(to) && king.getCell().equals(to) && rook.getCell().equals(new Cell(5, 0)), "king should be able to move during castling");

        Rook rook2 = new Rook(white, new Cell(0, 0));
        King king2 = new King(white, new Cell(4, 0), board.getPieces(), board);
        Cell to2 = new Cell(2, 0);
        createCustomBoard(board, king2, rook2);

        // Test that white king can castle (castling on the left side)
        assertTrue(king2.castling(to2) && king2.getCell().equals(to2) && rook2.getCell().equals(new Cell(3, 0)), "king should be able to move during castling");
    }


    @Test
    @DisplayName("Check if the king is in check during castling")
    public void testCastlingCheck() {
        Board board = new Board(this);
        Queen queen = new Queen(white, new Cell(1, 4));
        King king = new King(black, new Cell(4, 7), board.getPieces(), board);
        Rook rook = new Rook(black, new Cell(0, 7));
        Cell to = new Cell(2, 7);

        createCustomBoard(board, queen, king, rook);

        assertFalse(king.castling(to) || king.getCell().equals(to) || rook.getCell().equals(new Cell(5, 7)), "king should not be able to castle because he is in check");

    }

    @Test
    @DisplayName("Check illegal castling when the king or the rook have already moved")
    public void testMovedPiecesCastling() {
        Board board = new Board(this);
        Cell from = new Cell(4, 0);
        Cell to = new Cell(6, 0);
        King king = new King(white, from, board.getPieces(), board);
        Rook rook = new Rook(white, new Cell(7, 0));
        createCustomBoard(board, king, rook);

        // Test that white king cannot castle because the king has already moved

        // move the king
        board.move(king.getCell().getX(), king.getCell().getY(), 5, 0, king.getColor());

        //move the king back
        board.move(5, 0, 4, 0, king.getColor());

        //try to castle
        assertFalse(king.castling(to) || king.getCell().equals(to) || rook.getCell().equals(new Cell(5, 0)), "king should not be able to castle because he has already moved");

        // Test that white king cannot castle because the rook has already moved
        Cell to2 = new Cell(2, 0);
        King king2 = new King(white, from, board.getPieces(), board);
        Rook rook2 = new Rook(white, new Cell(0, 0));
        createCustomBoard(board, king, rook);

        // move the rook
        board.move(rook2.getCell().getX(), rook2.getCell().getY(), 1, 0, rook2.getColor());

        //move the rook back
        board.move(1, 0, 0, 0, rook2.getColor());
        assertFalse(king.castling(to2) || king2.getCell().equals(to2) || rook2.getCell().equals(new Cell(3, 0)), "king should not be able to castle because the rook has already moved");

        // Test that white king cannot castle because there is a piece between the king and the rook
        Queen queen = new Queen(white, new Cell(5, 0));
        createCustomBoard(board, king, rook, queen);
        assertFalse(king.castling(to), "king should not be able to castle because there is a piece between the king and the rook");


    }

    @Test
    @DisplayName("Check pieces between the king and the rook during castling")
    public void testPiecesBetweenCastling() {
        Board board = new Board(this);
        Cell to = new Cell(6, 0);
        Cell to2 = new Cell(2, 0);
        King king = new King(white, new Cell(4, 0), board.getPieces(), board);
        Rook rookRight = new Rook(white, new Cell(7, 0));
        Rook rookLeft = new Rook(white, new Cell(0, 0));
        Knight knight = new Knight(white, new Cell(5, 0));
        Knight knight2 = new Knight(white, new Cell(1, 0));
        createCustomBoard(board, king, rookRight, rookLeft, knight, knight2);

        // Test that white king cannot castle because there is a piece between the king and the rook
        assertFalse(king.castling(to) || king.getCell().equals(to) || rookRight.getCell().equals(new Cell(5, 0)), "king should not be able to castle because there is a piece between the king and the rook");

        // Test that white king cannot castle because there is a piece between the king and the rook
        assertFalse(king.castling(to2) || king.getCell().equals(to2) || rookLeft.getCell().equals(new Cell(3, 0)), "king should not be able to castle because there is a piece between the king and the rook");
    }


    @Test
    @DisplayName("test if the queen correctly moves")
    public void testQueenLegalMove() {
        Board board = new Board(this);

        Queen whiteQueen = new Queen(white, new Cell(4, 4));

        createCustomBoard(board, whiteQueen);

        // Test valid diagonal move for queen
        assertTrue(board.move(whiteQueen.getCell().getX(), whiteQueen.getCell().getY(), 5, 5, whiteQueen.getColor()), "Queen : Legal move");
        assertTrue(board.move(5, 5, 7, 3, whiteQueen.getColor()), "Queen : Legal move");

        // Test valid horizontal move for queen
        assertTrue(board.move(whiteQueen.getCell().getX(), whiteQueen.getCell().getY(), 1, 3, whiteQueen.getColor()), "Queen : Legal move");

        // Test valid vertical move for queen
        //white queen
        assertTrue(board.move(whiteQueen.getCell().getX(), whiteQueen.getCell().getY(), 1, 1, whiteQueen.getColor()), "Queen : Legal move");
    }

    @Test
    @DisplayName("test if the queen did illegal moves")
    public void testQueenIllegalMove() {
        Board board = new Board(this);

        Queen whiteQueen = new Queen(white, new Cell(2, 2));

        // Test invalid move for queen
        //move right and up
        assertFalse(board.move(whiteQueen.getCell().getX(), whiteQueen.getCell().getY(), 3, 4, whiteQueen.getColor()), "Queen : Illegal move");

        //move left and down
        assertFalse(board.move(whiteQueen.getCell().getX(), whiteQueen.getCell().getY(), 0, 1, whiteQueen.getColor()), "Queen : Illegal move");


    }


    @Test
    @DisplayName("test pieces between moves for Queen")
    public void testPiecesBetweenMovesForQueen() {
        Board board = new Board(this);
        Cell from = new Cell(1, 1);
        Cell between = new Cell(2, 2);
        Cell to = new Cell(3, 3);

        Queen whiteQueen = new Queen(white, from);
        Knight whiteKnight = new Knight(white, between);
        createCustomBoard(board, whiteQueen, whiteKnight);

        // Test that white queen cannot move to cell(3,3) because there is a piece between the two cells
        assertFalse(board.move(whiteQueen.getCell().getX(), whiteQueen.getCell().getY(), to.getX(), to.getY(), whiteQueen.getColor()), "Queen : Illegal move");
    }


    //Rook tests

    @Test
    @DisplayName("test if the rook correctly moves")
    public void testRookLegalMove() {
        Board board = new Board(this);
        Rook whiteRook = new Rook(white, new Cell(0, 7));

        createCustomBoard(board, whiteRook);

        // Test valid horizontal move for rook
        //white rook
        assertTrue(board.move(whiteRook.getCell().getX(), whiteRook.getCell().getY(), 1, 7, whiteRook.getColor()), "The white rook should be able to move horizontally");

        // Test valid vertical move for rook
        //white rook
        assertTrue(board.move(whiteRook.getCell().getX(), whiteRook.getCell().getY(), 1, 2, whiteRook.getColor()), "The white rook should be able to move vertically");
    }

    @Test
    public void testRookIllegalMove() {
        Board board = new Board(this);
        Rook whiteRook = new Rook(white, new Cell(0, 0));
        createCustomBoard(board, whiteRook);

        // Test invalid diagonal move for rook
        //white rook
        assertFalse(board.move(whiteRook.getCell().getX(), whiteRook.getCell().getY(), 1, 1, whiteRook.getColor()), "white rook cannot move diagonally");

        // Test invalid L shape move for rook
        //black rook
        assertFalse(board.move(whiteRook.getCell().getX(), whiteRook.getCell().getY(), 1, 2, whiteRook.getColor()), "black rook cannot move in L shape");
    }

    @Test
    @DisplayName("test pieces between moves for Rook")
    public void testPiecesBetweenMovesForRook() {
        Board board = new Board(this);

        Cell from = new Cell(1, 1);
        Cell between = new Cell(1, 2);
        Cell to = new Cell(1, 3);

        Rook whiteRook = new Rook(white, from);
        Pawn blackPawn = new Pawn(black, between, board);
        createCustomBoard(board, whiteRook, blackPawn);

        // Test that white rook cannot move to cell(3,1) because there is a piece between the two cells
        assertFalse(board.move(whiteRook.getCell().getX(), whiteRook.getCell().getY(), to.getX(), to.getY(), whiteRook.getColor()), "white rook cannot move because there is a piece between the two cells");
    }

    //
    @Test
    @DisplayName("test if the pawn correctly moves forward")
    public void testPawnLegalMove() {
        Board board = new Board(this);
        Pawn whitePawn = new Pawn(white, new Cell(0, 1), board);
        createCustomBoard(board, whitePawn);

        // Test valid vertical move for pawn
        //white pawn
        assertTrue(board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), 0, 2, whitePawn.getColor()), "white pawn should be able to move forward");
    }

    @Test
    @DisplayName("test if the pawn cannot move horizontally")
    public void testIllegalHorizontalMove() {
        Board board = new Board(this);
        Pawn whitePawn = new Pawn(white, new Cell(0, 1), board);
        createCustomBoard(board, whitePawn);

        // Test invalid horizontal move for pawn
        assertFalse(board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), 0, 4, whitePawn.getColor()), "white pawn cannot move horizontally with a distance > 2");

        //Test other invalid move
        assertFalse(board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), 1, 2, whitePawn.getColor()), "white pawn should not be able to reach this cell");
    }

    @Test
    @DisplayName("test if the pawn cannot move backwards")
    public void testPawnIllegalBackwardMove() {
        Board board = new Board(this);
        Pawn whitePawn = new Pawn(white, new Cell(0, 1), board);
        createCustomBoard(board, whitePawn);

        // Test invalid backward move for pawn
        assertFalse(board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), 0, 0, whitePawn.getColor()), "white pawn cannot move backwards");
    }

    @Test
    @DisplayName("test that pawn second move cannot move two spaces forward")
    public void testIllegalSecondMove() {
        Board board = new Board(this);
        Cell cell = new Cell(0, 1);
        Cell cell1 = new Cell(0, 3);
        Cell cell2 = new Cell(0, 5);
        Pawn whitePawn = new Pawn(white, cell, board);

        createCustomBoard(board, whitePawn);
        board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), cell1.getX(), cell1.getY(), whitePawn.getColor());

        // Test invalid second move for pawn
        assertFalse(board.move(whitePawn.getCell().getX(), whitePawn.getCell().getX(), cell2.getX(), cell2.getY(), whitePawn.getColor()), "white pawn cannot move two spaces in second move");
    }

    @Test
    @DisplayName("test pieces between moves")
    public void testPiecesBetweenMoves() {
        Board board = new Board(this);
        Cell from = new Cell(0, 1);
        Cell between = new Cell(0, 2);
        Cell to = new Cell(0, 3);

        Pawn whitePawn = new Pawn(white, from, board);
        Pawn blackPawn = new Pawn(black, between, board);
        createCustomBoard(board, whitePawn, blackPawn);

        // Test invalid second move for pawn
        assertFalse(board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), to.getX(), to.getY(), whitePawn.getColor()), "white pawn cannot move because there is a piece between the two cells");
    }

    @Test
    @DisplayName("Test enpassant")
    public void testEnPassant() {
        Board board = new Board(this);
        Pawn whitePawn = new Pawn(white, new Cell(0, 1), board);
        Pawn blackPawn = new Pawn(black, new Cell(1, 3), board);

        createCustomBoard(board, whitePawn, blackPawn);
        Cell to = new Cell(0, 2);

        //simulate white move forward
        board.move(whitePawn.getCell().getX(), whitePawn.getCell().getY(), 0, 3, whitePawn.getColor());

        //test enpassant
        assertTrue(board.move(blackPawn.getCell().getX(), blackPawn.getCell().getY(), to.getX(), to.getY(), blackPawn.getColor()), "white pawn should be able to do enpassant");
    }


    //leave empty
    @Override
    public void updateRemovePiece(Cell cell) {}

    @Override
    public void updateAddPiece(Piece piece) {}

    @Override
    public void updateNextTurn() {}

    @Override
    public Piece updatePopUp(Piece p) {
        return null;
    }

    @Override
    public void updateInCheck(PlayerColor color) {
        String colorMsg = color == PlayerColor.WHITE ? "white" : "black";
        System.out.println(colorMsg + " king is in check");
    }
}