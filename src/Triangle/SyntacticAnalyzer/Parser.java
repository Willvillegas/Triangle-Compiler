/*
 * @(#)Parser.java                        2.1 2003/10/07
 *
 * Copyright (C) 1999, 2003 D.A. Watt and D.F. Brown
 * Dept. of Computing Science, University of Glasgow, Glasgow G12 8QQ Scotland
 * and School of Computer and Math Sciences, The Robert Gordon University,
 * St. Andrew Street, Aberdeen AB25 1HG, Scotland.
 * All rights reserved.
 *
 * This software is provided free for educational use only. It may
 * not be used for commercial purposes without the prior written permission
 * of the authors.
 */

package Triangle.SyntacticAnalyzer;

import Triangle.ErrorReporter;
import Triangle.AbstractSyntaxTrees.ActualParameter;
import Triangle.AbstractSyntaxTrees.ActualParameterSequence;
import Triangle.AbstractSyntaxTrees.ArrayAggregate;
import Triangle.AbstractSyntaxTrees.ArrayExpression;
import Triangle.AbstractSyntaxTrees.ArrayTypeDenoter;
import Triangle.AbstractSyntaxTrees.AssignCommand;
import Triangle.AbstractSyntaxTrees.BinaryExpression;
import Triangle.AbstractSyntaxTrees.CallCommand;
import Triangle.AbstractSyntaxTrees.CallExpression;
import Triangle.AbstractSyntaxTrees.CharacterExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteral;
import Triangle.AbstractSyntaxTrees.Command;
import Triangle.AbstractSyntaxTrees.ConstActualParameter;
import Triangle.AbstractSyntaxTrees.ConstDeclaration;
import Triangle.AbstractSyntaxTrees.ConstFormalParameter;
import Triangle.AbstractSyntaxTrees.Declaration;
import Triangle.AbstractSyntaxTrees.DotVname;
import Triangle.AbstractSyntaxTrees.EmptyActualParameterSequence;
import Triangle.AbstractSyntaxTrees.EmptyCommand;
import Triangle.AbstractSyntaxTrees.EmptyFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.Expression;
import Triangle.AbstractSyntaxTrees.FieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.FormalParameter;
import Triangle.AbstractSyntaxTrees.FormalParameterSequence;
import Triangle.AbstractSyntaxTrees.FuncActualParameter;
import Triangle.AbstractSyntaxTrees.FuncDeclaration;
import Triangle.AbstractSyntaxTrees.FuncFormalParameter;
import Triangle.AbstractSyntaxTrees.Identifier;
import Triangle.AbstractSyntaxTrees.IfCommand;
import Triangle.AbstractSyntaxTrees.IfExpression;
import Triangle.AbstractSyntaxTrees.IntegerExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteral;
import Triangle.AbstractSyntaxTrees.LetCommand;
import Triangle.AbstractSyntaxTrees.LetExpression;
import Triangle.AbstractSyntaxTrees.MultipleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleArrayAggregate;
import Triangle.AbstractSyntaxTrees.MultipleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.MultipleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.MultipleRecordAggregate;
import Triangle.AbstractSyntaxTrees.Operator;
import Triangle.AbstractSyntaxTrees.ProcActualParameter;
import Triangle.AbstractSyntaxTrees.ProcDeclaration;
import Triangle.AbstractSyntaxTrees.ProcFormalParameter;
import Triangle.AbstractSyntaxTrees.Program;
import Triangle.AbstractSyntaxTrees.RecordAggregate;
import Triangle.AbstractSyntaxTrees.RecordExpression;
import Triangle.AbstractSyntaxTrees.RecordTypeDenoter;
import Triangle.AbstractSyntaxTrees.SequentialCommand;
import Triangle.AbstractSyntaxTrees.SequentialDeclaration;
import Triangle.AbstractSyntaxTrees.SimpleTypeDenoter;
import Triangle.AbstractSyntaxTrees.SimpleVname;
import Triangle.AbstractSyntaxTrees.SingleActualParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleArrayAggregate;
import Triangle.AbstractSyntaxTrees.SingleFieldTypeDenoter;
import Triangle.AbstractSyntaxTrees.SingleFormalParameterSequence;
import Triangle.AbstractSyntaxTrees.SingleRecordAggregate;
import Triangle.AbstractSyntaxTrees.SubscriptVname;
import Triangle.AbstractSyntaxTrees.TypeDeclaration;
import Triangle.AbstractSyntaxTrees.TypeDenoter;
import Triangle.AbstractSyntaxTrees.UnaryExpression;
import Triangle.AbstractSyntaxTrees.VarActualParameter;
import Triangle.AbstractSyntaxTrees.VarDeclaration;
import Triangle.AbstractSyntaxTrees.VarFormalParameter;
import Triangle.AbstractSyntaxTrees.Vname;
import Triangle.AbstractSyntaxTrees.VnameExpression;
import Triangle.AbstractSyntaxTrees.WhileCommand;
//Added
import Triangle.AbstractSyntaxTrees.RepeatCommand;
import Triangle.AbstractSyntaxTrees.DoWhileCommand;
import Triangle.AbstractSyntaxTrees.ForCommand;
import Triangle.AbstractSyntaxTrees.CaseCommand;
import Triangle.AbstractSyntaxTrees.CaseAggregate;
import Triangle.AbstractSyntaxTrees.IntegerLiteralAggregate;
import Triangle.AbstractSyntaxTrees.CharacterLiteralAggregate;
import Triangle.AbstractSyntaxTrees.ElseCaseAggregate;
import Triangle.AbstractSyntaxTrees.CaseExpression;
import Triangle.AbstractSyntaxTrees.IntegerLiteralAggregateExpression;
import Triangle.AbstractSyntaxTrees.CharacterLiteralAggregateExpression;
import Triangle.AbstractSyntaxTrees.ElseCaseAggregateExpression;
import Triangle.AbstractSyntaxTrees.CallMethodExpression;
import Triangle.AbstractSyntaxTrees.FuncTypeDenoter;
import Triangle.AbstractSyntaxTrees.ProcTypeDenoter;
import java.util.ArrayList;
import java.util.List;

public class Parser {

  private Scanner lexicalAnalyser;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePosition previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    lexicalAnalyser = lexer;
    errorReporter = reporter;
    previousTokenPosition = new SourcePosition();
  }

// accept checks whether the current token matches tokenExpected.
// If so, fetches the next token.
// If not, reports a syntactic error.

  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.position;
      currentToken = lexicalAnalyser.scan();
    } else {
      syntacticError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  void acceptIt() {
    previousTokenPosition = currentToken.position;
    currentToken = lexicalAnalyser.scan();
  }

// start records the position of the start of a phrase.
// This is defined to be the position of the first
// character of the first token of the phrase.

  void start(SourcePosition position) {
    position.start = currentToken.position.start;
  }

// finish records the position of the end of a phrase.
// This is defined to be the position of the last
// character of the last token of the phrase.

  void finish(SourcePosition position) {
    position.finish = previousTokenPosition.finish;
  }

  void syntacticError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePosition pos = currentToken.position;
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

///////////////////////////////////////////////////////////////////////////////
//
// PROGRAMS
//
///////////////////////////////////////////////////////////////////////////////

  public Program parseProgram() {

    Program programAST = null;

    previousTokenPosition.start = 0;
    previousTokenPosition.finish = 0;
    currentToken = lexicalAnalyser.scan();

    try {
      Command cAST = parseCommand();
      programAST = new Program(cAST, previousTokenPosition);
      if (currentToken.kind != Token.EOT) {
        syntacticError("\"%\" not expected after end of program",
          currentToken.spelling);
      }
    }
    catch (SyntaxError s) { return null; }
    return programAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// LITERALS
//
///////////////////////////////////////////////////////////////////////////////

// parseIntegerLiteral parses an integer-literal, and constructs
// a leaf AST to represent it.

  IntegerLiteral parseIntegerLiteral() throws SyntaxError {
    IntegerLiteral IL = null;

    if (currentToken.kind == Token.INTLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      IL = new IntegerLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      IL = null;
      syntacticError("integer literal expected here", "");
    }
    return IL;
  }

// parseCharacterLiteral parses a character-literal, and constructs a leaf
// AST to represent it.

  CharacterLiteral parseCharacterLiteral() throws SyntaxError {
    CharacterLiteral CL = null;

    if (currentToken.kind == Token.CHARLITERAL) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      CL = new CharacterLiteral(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      CL = null;
      syntacticError("character literal expected here", "");
    }
    return CL;
  }

// parseIdentifier parses an identifier, and constructs a leaf AST to
// represent it.

  Identifier parseIdentifier() throws SyntaxError {
    Identifier I = null;

    if (currentToken.kind == Token.IDENTIFIER) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      I = new Identifier(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      I = null;
      syntacticError("identifier expected here", "");
    }
    return I;
  }

// parseOperator parses an operator, and constructs a leaf AST to
// represent it.

  Operator parseOperator() throws SyntaxError {
    Operator O = null;

    if (currentToken.kind == Token.OPERATOR) {
      previousTokenPosition = currentToken.position;
      String spelling = currentToken.spelling;
      O = new Operator(spelling, previousTokenPosition);
      currentToken = lexicalAnalyser.scan();
    } else {
      O = null;
      syntacticError("operator expected here", "");
    }
    return O;
  }

///////////////////////////////////////////////////////////////////////////////
//
// COMMANDS
//
///////////////////////////////////////////////////////////////////////////////

// parseCommand parses the command, and constructs an AST
// to represent its phrase structure.

  Command parseCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();

    start(commandPos);
    commandAST = parseSingleCommand();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Command c2AST = parseSingleCommand();
      finish(commandPos);
      commandAST = new SequentialCommand(commandAST, c2AST, commandPos);
    }
    return commandAST;
  }

  Command parseSingleCommand() throws SyntaxError {
    Command commandAST = null; // in case there's a syntactic error

    SourcePosition commandPos = new SourcePosition();
    start(commandPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(commandPos);
          commandAST = new CallCommand(iAST, apsAST, commandPos);

        } else {
          //Becomes ::= V-name := Expression
          Vname vAST = parseRestOfVname(iAST);
          accept(Token.BECOMES);
          Expression eAST = parseExpression();
          finish(commandPos);
          commandAST = new AssignCommand(vAST, eAST, commandPos);
        }
      }
      break;

    case Token.BEGIN:
      acceptIt();
      commandAST = parseCommand();
      accept(Token.END);
      break;

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Command cAST = parseSingleCommand();
        finish(commandPos);
        commandAST = new LetCommand(dAST, cAST, commandPos);
      }
      break;

    case Token.IF:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.THEN);
        Command c1AST = parseSingleCommand();
        accept(Token.ELSE);
        Command c2AST = parseSingleCommand();
        finish(commandPos);
        commandAST = new IfCommand(eAST, c1AST, c2AST, commandPos);
      }
      break;

    case Token.WHILE:
      {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.DO);
        Command cAST = parseSingleCommand();
        finish(commandPos);
        commandAST = new WhileCommand(eAST, cAST, commandPos);
      }
      break;
    /**
     * TODO
     * Add RepeatCommand    //Ready
     * Add ForCommand       //Ready
     * Add DoCommand        //Ready
     * Add CaseCommand      //Ready
     **/
     //Repeat ::= repeat Command until Expression 
    case Token.REPEAT:{
        acceptIt();
        Command cAst = parseSingleCommand();
        accept(Token.UNTIL);
        Expression eAst = parseExpression();
        finish(commandPos);
        //Creacion final del AST
        commandAST = new RepeatCommand(cAst,eAst,commandPos);
    }
    break;
    //Do     ::= do Command while Expression
    case Token.DO:{
        acceptIt();
        Command cAst = parseSingleCommand();
        accept(Token.WHILE);
        Expression eAst = parseExpression();
        finish(commandPos);
        //Creacion final del AST
        commandAST = new DoWhileCommand(cAst,eAst,commandPos);
    }
    break;
    /**
     *  For    ::=  for V-name := Expression to Expression  
                    [by Integer-Literal]  
                    do  single-Command 
     */
    case Token.FOR:{
        commandAST= parseForCommand();       
    }
    break;
    /**
     * case-Command ::= case V-name of 
                    ( (Integer-Literal | Character-Literal) : single-Command )+ 
                 end 
     */
    case Token.CASE:{
        acceptIt();
        Vname vAST = parseVname();
        accept(Token.OF);
        CaseAggregate caAST = parseCaseAggregate();
        finish(commandPos);
        commandAST = new CaseCommand (vAST,caAST,commandPos);        
    }
    break;
    case Token.SEMICOLON:
    case Token.END:
    case Token.ELSE:
    case Token.IN:
    case Token.EOT:

      finish(commandPos);
      commandAST = new EmptyCommand(commandPos);
      break;

    default:
      syntacticError("\"%\" cannot start a command",
        currentToken.spelling);
      break;

    }

    return commandAST;
  }
  /**
   * Genera el arbol sintactico para el comando for, basado en los tokens que contiene el archivo analizado.
   * @return el arbol sintactico del forCommand
   * @throws SyntaxError 
   */
  Command parseForCommand() throws SyntaxError{
      Command commandAST = null; //Case error
      SourcePosition commandPos = new SourcePosition();
      start(commandPos);
      /**
       * For    ::=  for V-name := Expression to Expression  
                    [by Integer-Literal]  
                    do  single-Command 
       */
      // for V-Name
      accept(Token.FOR);
      Vname vAST = parseVname();
      // := Expression
      accept(Token.BECOMES);
      Expression e1AST = parseExpression();
      // to Expression
      accept(Token.TO);
      Expression e2AST = parseExpression();
      // [ by Integer-Literal ]
      IntegerLiteral byInteger = null;
      if (currentToken.kind == Token.BY){
          acceptIt();
          byInteger = parseIntegerLiteral();
      }
      // do Command
      accept(Token.DO);
      Command cAST = parseSingleCommand();
      commandAST = new ForCommand(vAST ,e1AST ,e2AST ,byInteger ,cAST ,commandPos);
      return commandAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// EXPRESSIONS
//
///////////////////////////////////////////////////////////////////////////////

  Expression parseExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();

    start (expressionPos);

    switch (currentToken.kind) {

    case Token.LET:
      {
        acceptIt();
        Declaration dAST = parseDeclaration();
        accept(Token.IN);
        Expression eAST = parseExpression();
        finish(expressionPos);
        expressionAST = new LetExpression(dAST, eAST, expressionPos);
      }
      break;

    case Token.IF:
      {
        acceptIt();
        Expression e1AST = parseExpression();
        accept(Token.THEN);
        Expression e2AST = parseExpression();
        accept(Token.ELSE);
        Expression e3AST = parseExpression();
        finish(expressionPos);
        expressionAST = new IfExpression(e1AST, e2AST, e3AST, expressionPos);
      }
      break;
    /**
     * Extended Triangle Compiler
     * CaseExpression       //Ready
     */
    /**
     * case-Expression ::= case V-name of  
                    ( (Integer-Literal | Character-Literal) : Expression )+ 
                     end 
     */
    case Token.CASE : {
        acceptIt();
        Vname vAST = parseVname();
        accept(Token.OF);
        CaseAggregate caAST = parseCaseAggregateExpression(vAST);
        finish(expressionPos);
        expressionAST = new CaseExpression (vAST,caAST,expressionPos);
    }
      break;
    default:
      expressionAST = parseSecondaryExpression();
      break;
    }
    return expressionAST;
  }

  Expression parseSecondaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    expressionAST = parsePrimaryExpression();
    while (currentToken.kind == Token.OPERATOR) {
      Operator opAST = parseOperator();
      Expression e2AST = parsePrimaryExpression();
      expressionAST = new BinaryExpression (expressionAST, opAST, e2AST,
        expressionPos);
    }
    return expressionAST;
  }

  Expression parsePrimaryExpression() throws SyntaxError {
    Expression expressionAST = null; // in case there's a syntactic error

    SourcePosition expressionPos = new SourcePosition();
    start(expressionPos);

    switch (currentToken.kind) {

    case Token.INTLITERAL:
      {
        IntegerLiteral ilAST = parseIntegerLiteral();
        finish(expressionPos);
        expressionAST = new IntegerExpression(ilAST, expressionPos);
      }
      break;

    case Token.CHARLITERAL:
      {
        CharacterLiteral clAST= parseCharacterLiteral();
        finish(expressionPos);
        expressionAST = new CharacterExpression(clAST, expressionPos);
      }
      break;

    case Token.LBRACKET:
      {
        acceptIt();
        ArrayAggregate aaAST = parseArrayAggregate();
        accept(Token.RBRACKET);
        finish(expressionPos);
        expressionAST = new ArrayExpression(aaAST, expressionPos);
      }
      break;

    case Token.LCURLY:
      {
        acceptIt();
        RecordAggregate raAST = parseRecordAggregate();
        accept(Token.RCURLY);
        finish(expressionPos);
        expressionAST = new RecordExpression(raAST, expressionPos);
      }
      break;

    case Token.IDENTIFIER:
      {
        Identifier iAST= parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(iAST, apsAST, expressionPos);

        } else if(this.currentToken.kind==Token.COLOND){
            this.acceptIt();
            Identifier  mAST= this.parseIdentifier();
            ActualParameterSequence apsAST=null;
            if(this.currentToken.kind== Token.LPAREN){
                this.acceptIt();
                apsAST= this.parseActualParameterSequence();
                this.accept(Token.RPAREN);
            }
            this.finish(expressionPos);
            Vname vAST=this.parseRestOfVname(iAST);
            expressionAST=new CallMethodExpression(vAST,mAST,apsAST,expressionPos);
        }else{
          Vname vAST = parseRestOfVname(iAST);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }
        /*Identifier iAST= parseIdentifier();
        if (currentToken.kind == Token.LPAREN) {
          acceptIt();
          ActualParameterSequence apsAST = parseActualParameterSequence();
          accept(Token.RPAREN);
          finish(expressionPos);
          expressionAST = new CallExpression(iAST, apsAST, expressionPos);

        } else {
          Vname vAST = parseRestOfVname(iAST);
          finish(expressionPos);
          expressionAST = new VnameExpression(vAST, expressionPos);
        }*/
      }
      break;

    case Token.OPERATOR:
      {
        Operator opAST = parseOperator();
        Expression eAST = parsePrimaryExpression();
        finish(expressionPos);
        expressionAST = new UnaryExpression(opAST, eAST, expressionPos);
      }
      break;

    case Token.LPAREN:
      acceptIt();
      expressionAST = parseExpression();
      accept(Token.RPAREN);
      break;

    default:
      syntacticError("\"%\" cannot start an expression",
        currentToken.spelling);
      break;

    }
    return expressionAST;
  }
  
  /**
   * AGGREGATES TRIANGLE COMPILER
   * 
   */

  RecordAggregate parseRecordAggregate() throws SyntaxError {
    RecordAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Identifier iAST = parseIdentifier();
    accept(Token.IS);
    Expression eAST = parseExpression();

    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      RecordAggregate aAST = parseRecordAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleRecordAggregate(iAST, eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleRecordAggregate(iAST, eAST, aggregatePos);
    }
    return aggregateAST;
  }

  ArrayAggregate parseArrayAggregate() throws SyntaxError {
    ArrayAggregate aggregateAST = null; // in case there's a syntactic error

    SourcePosition aggregatePos = new SourcePosition();
    start(aggregatePos);

    Expression eAST = parseExpression();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ArrayAggregate aAST = parseArrayAggregate();
      finish(aggregatePos);
      aggregateAST = new MultipleArrayAggregate(eAST, aAST, aggregatePos);
    } else {
      finish(aggregatePos);
      aggregateAST = new SingleArrayAggregate(eAST, aggregatePos);
    }
    return aggregateAST;
  }
  
  CaseAggregate parseCaseAggregate() throws SyntaxError {
      CaseAggregate aggregateAST = null; // in case there's a syntactic error
      SourcePosition aggregatePos = new SourcePosition();
      start(aggregatePos);
      if (currentToken.kind == Token.INTLITERAL) {
          IntegerLiteral ilAST = parseIntegerLiteral();
          accept(Token.COLON);
          Command cAST = parseSingleCommand();
          accept(Token.SEMICOLON);
          CaseAggregate aAST = parseCaseAggregate();
          finish(aggregatePos);
          aggregateAST = new IntegerLiteralAggregate(ilAST, cAST, aAST, aggregatePos);
      } else if (currentToken.kind == Token.CHARLITERAL){
          CharacterLiteral clAST = parseCharacterLiteral();
          accept(Token.COLON);
          Command cAST = parseSingleCommand();
          accept(Token.SEMICOLON);
          CaseAggregate aAST = parseCaseAggregate();
          finish(aggregatePos);
          aggregateAST = new CharacterLiteralAggregate(clAST, cAST, aAST, aggregatePos);
      } else {
          accept(Token.ELSE);
          accept(Token.COLON);
          Command cAST = parseSingleCommand();
          finish(aggregatePos);
          aggregateAST = new ElseCaseAggregate(cAST, aggregatePos);
      }
      return aggregateAST;
  }
  CaseAggregate parseCaseAggregateExpression(Vname vAST) throws SyntaxError {
      CaseAggregate aggregateAST = null; // in case there's a syntactic error
      SourcePosition aggregatePos = new SourcePosition();
      start(aggregatePos);
      if (currentToken.kind == Token.INTLITERAL) {
          IntegerLiteral ilAST = parseIntegerLiteral();
          accept(Token.COLON);
          Expression eAST = parseExpression();
          accept(Token.SEMICOLON);
          CaseAggregate aAST = parseCaseAggregateExpression(vAST);
          finish(aggregatePos);
          aggregateAST = new IntegerLiteralAggregateExpression(vAST,ilAST, eAST, aAST, aggregatePos);
      } else if (currentToken.kind == Token.CHARLITERAL){
          CharacterLiteral clAST = parseCharacterLiteral();
          accept(Token.COLON);
          Expression eAST = parseExpression();
          accept(Token.SEMICOLON);
          CaseAggregate aAST = parseCaseAggregateExpression(vAST);
          finish(aggregatePos);
          aggregateAST = new CharacterLiteralAggregateExpression(vAST,clAST, eAST, aAST, aggregatePos);
      } else {
          accept(Token.ELSE);
          accept(Token.COLON);
          Expression eAST = parseExpression();
          finish(aggregatePos);
          aggregateAST = new ElseCaseAggregateExpression(eAST, aggregatePos);
      }
      return aggregateAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// VALUE-OR-VARIABLE NAMES
//
///////////////////////////////////////////////////////////////////////////////

  Vname parseVname () throws SyntaxError {
    Vname vnameAST = null; // in case there's a syntactic error
    Identifier iAST = parseIdentifier();
    vnameAST = parseRestOfVname(iAST);
    return vnameAST;
  }

  Vname parseRestOfVname(Identifier identifierAST) throws SyntaxError {
    SourcePosition vnamePos = new SourcePosition();
    vnamePos = identifierAST.position;
    Vname vAST = new SimpleVname(identifierAST, vnamePos);

    while (currentToken.kind == Token.DOT ||
           currentToken.kind == Token.LBRACKET) {

      if (currentToken.kind == Token.DOT) {
        acceptIt();
        Identifier iAST = parseIdentifier();
        vAST = new DotVname(vAST, iAST, vnamePos);        
      } else {
        acceptIt();
        Expression eAST = parseExpression();
        accept(Token.RBRACKET);
        finish(vnamePos);
        vAST = new SubscriptVname(vAST, eAST, vnamePos);
      }
    }
    return vAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// DECLARATIONS
//
///////////////////////////////////////////////////////////////////////////////

  Declaration parseDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error

    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);
    declarationAST = parseSingleDeclaration();
    while (currentToken.kind == Token.SEMICOLON) {
      acceptIt();
      Declaration d2AST = parseSingleDeclaration();
      finish(declarationPos);
      declarationAST = new SequentialDeclaration(declarationAST, d2AST,
        declarationPos);
    }
    return declarationAST;
  }

  Declaration parseSingleDeclaration() throws SyntaxError {
    Declaration declarationAST = null; // in case there's a syntactic error
    
    SourcePosition declarationPos = new SourcePosition();
    start(declarationPos);

    switch (currentToken.kind) {

    case Token.CONST:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new ConstDeclaration(iAST, eAST, declarationPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new VarDeclaration(iAST, tAST, declarationPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseSingleCommand();
        finish(declarationPos);
        declarationAST = new ProcDeclaration(iAST, fpsAST, cAST, declarationPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(declarationPos);
        declarationAST = new FuncDeclaration(iAST, fpsAST, tAST, eAST,
          declarationPos);
      }
      break;

    case Token.TYPE:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.IS);
        TypeDenoter tAST = parseTypeDenoter();
        finish(declarationPos);
        declarationAST = new TypeDeclaration(iAST, tAST, declarationPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a declaration",
        currentToken.spelling);
      break;

    }
    return declarationAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// PARAMETERS
//
///////////////////////////////////////////////////////////////////////////////

  FormalParameterSequence parseFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST;

    SourcePosition formalsPos = new SourcePosition();

    start(formalsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(formalsPos);
      formalsAST = new EmptyFormalParameterSequence(formalsPos);

    } else {
      formalsAST = parseProperFormalParameterSequence();
    }
    return formalsAST;
  }

  FormalParameterSequence parseProperFormalParameterSequence() throws SyntaxError {
    FormalParameterSequence formalsAST = null; // in case there's a syntactic error;

    SourcePosition formalsPos = new SourcePosition();
    start(formalsPos);
    FormalParameter fpAST = parseFormalParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FormalParameterSequence fpsAST = parseProperFormalParameterSequence();
      finish(formalsPos);
      formalsAST = new MultipleFormalParameterSequence(fpAST, fpsAST,
        formalsPos);

    } else {
      finish(formalsPos);
      formalsAST = new SingleFormalParameterSequence(fpAST, formalsPos);
    }
    return formalsAST;
  }

  FormalParameter parseFormalParameter() throws SyntaxError {
    FormalParameter formalAST = null; // in case there's a syntactic error;

    SourcePosition formalPos = new SourcePosition();
    start(formalPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new ConstFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new VarFormalParameter(iAST, tAST, formalPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        finish(formalPos);
        formalAST = new ProcFormalParameter(iAST, fpsAST, formalPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tAST = parseTypeDenoter();
        finish(formalPos);
        formalAST = new FuncFormalParameter(iAST, fpsAST, tAST, formalPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start a formal parameter",
        currentToken.spelling);
      break;

    }
    return formalAST;
  }


  ActualParameterSequence parseActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST;

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    if (currentToken.kind == Token.RPAREN) {
      finish(actualsPos);
      actualsAST = new EmptyActualParameterSequence(actualsPos);

    } else {
      actualsAST = parseProperActualParameterSequence();
    }
    return actualsAST;
  }

  ActualParameterSequence parseProperActualParameterSequence() throws SyntaxError {
    ActualParameterSequence actualsAST = null; // in case there's a syntactic error

    SourcePosition actualsPos = new SourcePosition();

    start(actualsPos);
    ActualParameter apAST = parseActualParameter();
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      ActualParameterSequence apsAST = parseProperActualParameterSequence();
      finish(actualsPos);
      actualsAST = new MultipleActualParameterSequence(apAST, apsAST,
        actualsPos);
    } else {
      finish(actualsPos);
      actualsAST = new SingleActualParameterSequence(apAST, actualsPos);
    }
    return actualsAST;
  }

  ActualParameter parseActualParameter() throws SyntaxError {
    ActualParameter actualAST = null; // in case there's a syntactic error

    SourcePosition actualPos = new SourcePosition();

    start(actualPos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
    case Token.INTLITERAL:
    case Token.CHARLITERAL:
    case Token.OPERATOR:
    case Token.LET:
    case Token.IF:
    case Token.LPAREN:
    case Token.LBRACKET:
    case Token.LCURLY:
      {
        Expression eAST = parseExpression();
        finish(actualPos);
        actualAST = new ConstActualParameter(eAST, actualPos);
      }
      break;

    case Token.VAR:
      {
        acceptIt();
        Vname vAST = parseVname();
        finish(actualPos);
        actualAST = new VarActualParameter(vAST, actualPos);
      }
      break;

    case Token.PROC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new ProcActualParameter(iAST, actualPos);
      }
      break;

    case Token.FUNC:
      {
        acceptIt();
        Identifier iAST = parseIdentifier();
        finish(actualPos);
        actualAST = new FuncActualParameter(iAST, actualPos);
      }
      break;

    default:
      syntacticError("\"%\" cannot start an actual parameter",
        currentToken.spelling);
      break;

    }
    return actualAST;
  }

///////////////////////////////////////////////////////////////////////////////
//
// TYPE-DENOTERS
//
///////////////////////////////////////////////////////////////////////////////
/*
  
    private FuncDeclaration parseFuncFieldDeclaration(Identifier id) throws SyntaxError {
    SourcePosition funcPos = new SourcePosition();
    start(funcPos);

    accept(Token.LPAREN);
    FormalParameterSequence fps = parseFormalParameterSequence();
    accept(Token.RPAREN);
    accept(Token.COLON);
    TypeDenoter td = parseTypeDenoter();
    accept(Token.IS);
    Expression e = parseExpression();

    finish(funcPos);
    return new FuncDeclaration(id, fps, td, e, funcPos, null);
  }


  private ProcDeclaration parseProcFieldDeclaration(Identifier id) throws SyntaxError {
    SourcePosition procPos = new SourcePosition();
    start(procPos);

    accept(Token.LPAREN);
    FormalParameterSequence fps = parseFormalParameterSequence();
    accept(Token.RPAREN);
    accept(Token.IS);
    Command c = parseSingleCommand();

    finish(procPos);
    return new ProcDeclaration(id, fps, c, procPos, null);
  }
  
  */
  
  
  
  
  
  TypeDenoter parseTypeDenoter() throws SyntaxError {
    TypeDenoter typeAST = null; // in case there's a syntactic error
    SourcePosition typePos = new SourcePosition();
    

    start(typePos);

    switch (currentToken.kind) {

    case Token.IDENTIFIER:
      {
        Identifier iAST = parseIdentifier();
        finish(typePos);
        typeAST = new SimpleTypeDenoter(iAST, typePos);
      }
      break;

    case Token.ARRAY:
      {
        acceptIt();
        IntegerLiteral ilAST = parseIntegerLiteral();
        accept(Token.OF);
        TypeDenoter tAST = parseTypeDenoter();
        finish(typePos);
        typeAST = new ArrayTypeDenoter(ilAST, tAST, typePos);
      }
      break;

    case Token.RECORD:
      { 
        
        /*acceptIt();
        FieldTypeDenoter fAST = null;
        //Listas de Proc y Func para cada caso
        //List<ProcDeclaration> proc = new ArrayList<>();
        //List<FuncDeclaration> func = new ArrayList<>();
        while (this.currentToken.kind != Token.END){
            if (this.currentToken.kind == Token.IDENTIFIER){
                Identifier iAST = parseIdentifier();
                accept(Token.COLON);
                switch (this.currentToken.kind){
                    case Token.FUNC :{
                        acceptIt();
                        
                        //FuncDeclaration fdAST = parseFuncDeclaration(iAST);
                        //func.add(fdAST);
                        accept(Token.COMMA);
                    }
                    break;
                    case Token.PROC :{
                        acceptIt();
                        //ProcDeclaration pdAST = parseProcDeclaration(iAST);
                        //proc.add(pdAST);
                        accept(Token.COMMA);
                    }
                    break;
                    default:
                        TypeDenoter tAST = parseTypeDenoter();
                        fAST = (fAST != null)?(new MultipleFieldTypeDenoter(iAST,tAST,fAST,this.currentToken.position)) : 
                                              (new SingleFieldTypeDenoter(iAST,tAST,this.currentToken.position));
                        accept(Token.COMMA);
                    break;
                }
            }else{
                syntacticError("\"%\" cannot start a type denoter",
                currentToken.spelling);
            }
        }
        
        
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST,func,proc, typePos);
        /*
        acceptIt(); // Aceptar "record"

        FieldTypeDenoter fAST = null;
        List<FuncDeclaration> funcList = new ArrayList<>();
        List<ProcDeclaration> procList = new ArrayList<>();

        while (currentToken.kind != Token.END) {
          if (currentToken.kind == Token.IDENTIFIER) {
            Identifier id = parseIdentifier();
            accept(Token.COLON);

            if (currentToken.kind == Token.FUNC) {
              acceptIt();
              FuncDeclaration funcDecl = parseFuncFieldDeclaration(id);
              funcList.add(funcDecl);
              accept(Token.SEMICOLON);
            } else if (currentToken.kind == Token.PROC) {
              acceptIt();
              ProcDeclaration procDecl = parseProcFieldDeclaration(id);
              procList.add(procDecl);
              accept(Token.SEMICOLON);
            } else {
              TypeDenoter tAST = parseTypeDenoter();
              if (fAST == null) {
                fAST = new SingleFieldTypeDenoter(id, tAST, currentToken.position);
              } else {
                fAST = new MultipleFieldTypeDenoter(id, tAST, fAST, currentToken.position);
              }
              accept(Token.SEMICOLON);
            }
          } else {
            syntacticError("Identifier expected", "");
          }
        }

        accept(Token.END); // Aceptar "end" al final del record
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, funcList, procList, typePos);*/
        acceptIt();
        FieldTypeDenoter fAST = parseFieldTypeDenoter();
        accept(Token.END);
        finish(typePos);
        typeAST = new RecordTypeDenoter(fAST, typePos);
      }
      break;
    
    case Token.FUNC :{
        this.acceptIt();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.COLON);
        TypeDenoter tdAST = parseTypeDenoter();
        accept(Token.IS);
        Expression eAST = parseExpression();
        finish(typePos);
        typeAST = new FuncTypeDenoter(fpsAST,tdAST,eAST,typePos);
    }
    break;
    
    case Token.PROC :{
        this.acceptIt();
        accept(Token.LPAREN);
        FormalParameterSequence fpsAST = parseFormalParameterSequence();
        accept(Token.RPAREN);
        accept(Token.IS);
        Command cAST = parseSingleCommand();
        finish(typePos);
        typeAST = new ProcTypeDenoter(fpsAST,cAST,typePos);
    }
    break;
    default:
      syntacticError("\"%\" cannot start a type denoter",
        currentToken.spelling);
      break;

    }
    return typeAST;
  }

  FieldTypeDenoter parseFieldTypeDenoter() throws SyntaxError {
    FieldTypeDenoter fieldAST = null; // in case there's a syntactic error

    SourcePosition fieldPos = new SourcePosition();

    start(fieldPos);
    Identifier iAST = parseIdentifier();
    accept(Token.COLON);
    TypeDenoter tAST = parseTypeDenoter();//parseRecordType(iAST);
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      FieldTypeDenoter fAST = parseFieldTypeDenoter();
      finish(fieldPos);
      fieldAST = new MultipleFieldTypeDenoter(iAST, tAST, fAST, fieldPos);
    } else {
      finish(fieldPos);
      fieldAST = new SingleFieldTypeDenoter(iAST, tAST, fieldPos);
    }
    return fieldAST;
  }

  /*
  ProcDeclaration parseProcDeclaration(Identifier iast) throws SyntaxError{
      ProcDeclaration pdAST = null;
      SourcePosition declarationPos = new SourcePosition();
      start(declarationPos);
      accept(Token.LPAREN);
      FormalParameterSequence fpsAST = parseFormalParameterSequence();
      accept(Token.RPAREN);
      accept(Token.IS);
      Command cAST = parseSingleCommand();
      finish(declarationPos);
      pdAST = new ProcDeclaration(iast,fpsAST,cAST,declarationPos);
      return pdAST;
  }
  
  FuncDeclaration parseFuncDeclaration (Identifier iast) throws SyntaxError{
      FuncDeclaration fdAST = null; //case SyntaxError
      SourcePosition declarationPos = new SourcePosition();
      start(declarationPos);
      accept(Token.LPAREN);
      FormalParameterSequence fpsAST = parseFormalParameterSequence();
      accept(Token.RPAREN);
      accept(Token.COLON);
      TypeDenoter tAST = parseTypeDenoter();
      accept(Token.IS);
      Expression eAST = parseExpression();
      finish(declarationPos);
      fdAST = new FuncDeclaration(iast,fpsAST,tAST,eAST,declarationPos);
      return fdAST;
  }*/
}
