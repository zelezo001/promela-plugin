package cz.cuni.mff.gitlab.zelezno.promelaplugin.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaMacroTypes.*;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;

%%

%{
  public PromelaLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class PromelaLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\n+
WHITE_SPACE=[ \t\x0B\f\r]+
UNAME=[A-Za-z_][A-Za-z_0-9]*
NUMBER=[0-9]+
LINE_ESCAPE=\\(\r)?\n
LINE_COMMENT="//"([^\r\n]|{LINE_ESCAPE})*
BLOCK_COMMENT="/"[*]([*]+[^*/]|[^*])*[*]+"/"
PREPROCESSOR_COMMENT=#.*
ESCAPE={LINE_ESCAPE}|\\[^\r\n]
CHAR='([^\\'\r\n]|{ESCAPE})'
STRING = \"([^\\\"\r\n]|{ESCAPE})*(\")?
%state DEFINE
%state DEFINE_BODY
%state UNSUPPORTED_MACRO
%state CONDITION_BODY
%%
// only simple #define name value supported for now
<DEFINE> {
{UNAME}                       { yybegin(DEFINE_BODY); return PREPROCESSOR_DEFINE_NAME; }
{WHITE_SPACE}                 { return WHITE_SPACE; }
}


<DEFINE,DEFINE_BODY,CONDITION_BODY> {
  "\\\n" {return EOL;}
  {EOL}  {yybegin(YYINITIAL); return PREPROCESSOR_DIRECTIVE_END;}
}

<UNSUPPORTED_MACRO> {
  "\\\n" {return PREPROCESSOR_COMMENT;} // macros can span over multiple lines ending with \
  .      {return PREPROCESSOR_COMMENT;}
  {EOL}  {yybegin(YYINITIAL); return PREPROCESSOR_COMMENT;}
}

<YYINITIAL> {
  {EOL}                                { return EOL; }
  ^{WHITE_SPACE}?#(define|DEFINE)      { yybegin(DEFINE); return PREPROCESSOR_DEFINE;}
  ^{WHITE_SPACE}?#(if|IF)              { yybegin(CONDITION_BODY); return PREPROCESSOR_IF;}
  ^{WHITE_SPACE}?#(ifdef|IFDEF)        { yybegin(DEFINE); return PREPROCESSOR_IFDEF;}
  ^{WHITE_SPACE}?#(ifndef|IFNDEF)      { yybegin(DEFINE); return PREPROCESSOR_IFNDEF;}
  ^{WHITE_SPACE}?#(elif|ELIF)          { yybegin(CONDITION_BODY); return PREPROCESSOR_ELSEIF;}
  ^{WHITE_SPACE}?#(else|ELSE)          { yybegin(DEFINE_BODY); return PREPROCESSOR_ELSE;}
  ^{WHITE_SPACE}?#(endif|ENDIF)        { yybegin(DEFINE_BODY); return PREPROCESSOR_ENDIF;}
  ^{WHITE_SPACE}?#(undef|UNDEF)        { yybegin(DEFINE); return PREPROCESSOR_UNDEFINE;}
  ^{WHITE_SPACE}?#                     { yybegin(UNSUPPORTED_MACRO); return PREPROCESSOR_COMMENT;}
}

<YYINITIAL,DEFINE_BODY> {
  "++"                         { return INCDEC; }
  "--"                         { return INCDEC; }
  "::"                         { return SEP; }
  ";"                          { return SEMI; }
  "->"                         { return ARROW; }
  ","                          { return COMMA; }
  ":"                          { return COL; }
  ".."                         { return DOTDOT; }
  "."                          { return DOT; }
  "@"                          { return AT; }
  "?"                          { return QMARK; }
  "{"                          { return OPENCURLYBRACKET; }
  "}"                          { return CLOSECURLYBRACKET; }
  "["                          { return OPENSQUAREBRACKET; }
  "]"                          { return CLOSESQUAREBRACKET; }
  "inline"                     { return INLINE_KW; }
  "proctype"                   { return PROCTYPE_KW; }
  "D_proctype"                 { return PROCTYPE_D_KW; }
  "init"                       { return INIT_KW; }
  "never"                      { return NEVER_KW; }
  "trace"                      { return TRACE_KW; }
  "notrace"                    { return NOTRACE_KW; }
  "typedef"                    { return TYPEDEF_KW; }
  "mtype"                      { return MTYPE_KW; }
  "unsigned"                   { return UNSIGNED_KW; }
  "bit"                        { return BIT_TYPE_KW; }
  "bool"                       { return BOOL_TYPE_KW; }
  "byte"                       { return BYTE_TYPE_KW; }
  "pid"                        {return BYTE_TYPE_KW; } // byte alias
  "short"                      { return SHORT_TYPE_KW; }
  "int"                        { return INT_TYPE_KW; }
  "chan"                       { return CHAN_TYPE_KW; }
  "active"                     { return ACTIVE_KW; }
  "priority"                   { return PRIORITY_KW; }
  "provided"                   { return PROVIDED_KW; }
  "hidden"                     { return HIDDEN_KW; }
  "show"                       { return SHOW_KW; }
  "unless"                     { return UNLESS_KW; }
  "xr"                         { return XR_KW; }
  "xs"                         { return XS_KW; }
  "of"                         { return OF_KW; }
  "eval"                       { return EVAL_KW; }
  "do"                         { return DO_KW; }
  "od"                         { return DO_END_KW; }
  "if"                         { return IF_KW; }
  "fi"                         { return IF_END_KW; }
  "atomic"                     { return ATOMIC_KW; }
  "for"                        { return FOR_KW; }
  "assert"                     { return ASSERT_KW; }
  "d_step"                     { return D_STEP_KW; }
  "select"                     { return SELECT_KW; }
  "c_code"                     { return C_CODE_KW; }
  "c_expr"                     { return C_EXPR_KW; }
  "c_decl"                     { return C_DECL_KW; }
  "c_track"                    { return C_TRACK_KW; }
  "c_state"                    { return C_STATE_KW; }
  "else"                       { return ELSE_KW; }
  "break"                      { return BREAK_KW; }
  "goto"                       { return GOTO_KW; }
  "printf"                     { return PRINTF_KW; }
  "printm"                     { return PRINTM_KW; }
  "in"                         { return IN_KW; }
  "len"                        { return LEN_KW; }
  "timeout"                    { return TIMEOUT_KW; }
  "np_"                        { return NP_KW; }
  "enabled"                    { return ENABLED_KW; }
  "pc_value"                   { return PC_VALUE_KW; }
  "run"                        { return RUN_KW; }
  "get_priority"               { return GET_PRIORITY_KW; }
  "set_priority"               { return SET_PRIORITY_KW; }
  "true"                       { return CONST_TRUE; }
  "false"                      { return CONST_FALSE; }
  "skip"                       { return CONST_SKIP; }
  "full"                       { return CHANPOLL_FULL; }
  "empty"                      { return CHANPOLL_EMPTY; }
  "nfull"                      { return CHANPOLL_NFULL; }
  "nempty"                     { return CHANPOLL_NEMPTY; }
  "local"                      { return LOCAL_KW;}
  "ltl"                        { return LTL_KW; }
  "return"                     { return RETURN_KW; }
  {LINE_COMMENT}               { return LINE_COMMENT; }
  {BLOCK_COMMENT}              { return BLOCK_COMMENT; }
  {STRING}                     { return STRING; }
}

<YYINITIAL,DEFINE_BODY,CONDITION_BODY> {
  "("                          { return OPENBRACKET; }
  ")"                          { return CLOSEBRACKET; }
  "+"                          { return OP_PLUS; }
  "-"                          { return OP_MINUS; }
  "*"                          { return OP_STAR; }
  "/"                          { return OP_DIV; }
  "%"                          { return OP_MOD; }
  "&&"                         { return OP_AND; }
  "||"                         { return OP_OR; }
  "&"                          { return OP_BAND; }
  "^"                          { return OP_XOR; }
  "|"                          { return OP_BOR; }
  ">="                         { return OP_GE; }
  "<="                         { return OP_LE; }
  "<<"                         { return OP_SLEFT; }
  ">>"                         { return OP_SRIGHT; }
  ">"                          { return OP_GT; }
  "<"                          { return OP_LT; }
  "=="                         { return OP_EQ; }
  "!="                         { return OP_NEQ; }
  "="                          { return OP_ASGN; }
  "!"                          { return OP_BANG; }
  "~"                          { return OP_NEG; }
  {UNAME}                      { return UNAME; }
  {CHAR}                       { return NUMBER; }
  {NUMBER}                     { return NUMBER; }
  {WHITE_SPACE}                { return WHITE_SPACE; }
}

<DEFINE_BODY> {
  #{UNAME} {return STRING; }
}

[^] { return BAD_CHARACTER; }
