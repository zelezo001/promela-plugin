// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaElementType;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenType;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl.*;

public interface PromelaTypes {

  IElementType ACTIVE = new PromelaElementType("ACTIVE");
  IElementType ACTIVE_PRIORITY = new PromelaElementType("ACTIVE_PRIORITY");
  IElementType ADDITION_CONST_EXPRESSION = new PromelaElementType("ADDITION_CONST_EXPRESSION");
  IElementType ANY_EXPRESSION = new PromelaElementType("ANY_EXPRESSION");
  IElementType ARG_LST = new PromelaElementType("ARG_LST");
  IElementType ARRAY_INIT = new PromelaElementType("ARRAY_INIT");
  IElementType ASSIGN = new PromelaElementType("ASSIGN");
  IElementType ATOMIC_STMNT = new PromelaElementType("ATOMIC_STMNT");
  IElementType BASE_EXPRESSION = new PromelaElementType("BASE_EXPRESSION");
  IElementType BINARY_ANY_EXPRESSION = new PromelaElementType("BINARY_ANY_EXPRESSION");
  IElementType BINARY_BASE_EXPRESSION = new PromelaElementType("BINARY_BASE_EXPRESSION");
  IElementType CEXPR_ANY_EXPRESSION = new PromelaElementType("CEXPR_ANY_EXPRESSION");
  IElementType CHANPOLL = new PromelaElementType("CHANPOLL");
  IElementType CHANPOLL_BASE_EXPRESSION = new PromelaElementType("CHANPOLL_BASE_EXPRESSION");
  IElementType CH_INIT = new PromelaElementType("CH_INIT");
  IElementType CH_INIT_TYPES = new PromelaElementType("CH_INIT_TYPES");
  IElementType CONST = new PromelaElementType("CONST");
  IElementType CONST_ANY_EXPRESSION = new PromelaElementType("CONST_ANY_EXPRESSION");
  IElementType CONST_BASE_EXPRESSION = new PromelaElementType("CONST_BASE_EXPRESSION");
  IElementType CONST_EXPRESSION = new PromelaElementType("CONST_EXPRESSION");
  IElementType CUSTOM_TYPENAME = new PromelaElementType("CUSTOM_TYPENAME");
  IElementType DO_STMNT = new PromelaElementType("DO_STMNT");
  IElementType D_PROCTYPE = new PromelaElementType("D_PROCTYPE");
  IElementType ENABLED_ANY_EXPRESSION = new PromelaElementType("ENABLED_ANY_EXPRESSION");
  IElementType ENABLED_BASE_EXPRESSION = new PromelaElementType("ENABLED_BASE_EXPRESSION");
  IElementType ENABLER = new PromelaElementType("ENABLER");
  IElementType FOR_STMNT = new PromelaElementType("FOR_STMNT");
  IElementType GET_PRIORITY_ANY_EXPRESSION = new PromelaElementType("GET_PRIORITY_ANY_EXPRESSION");
  IElementType GET_PRIORITY_BASE_EXPRESSION = new PromelaElementType("GET_PRIORITY_BASE_EXPRESSION");
  IElementType GOTO = new PromelaElementType("GOTO");
  IElementType IDENTIFIER = new PromelaElementType("IDENTIFIER");
  IElementType IF_STMNT = new PromelaElementType("IF_STMNT");
  IElementType INDEXER = new PromelaElementType("INDEXER");
  IElementType INIT = new PromelaElementType("INIT");
  IElementType INLINE = new PromelaElementType("INLINE");
  IElementType INLINE_ARG = new PromelaElementType("INLINE_ARG");
  IElementType INLINE_CALL = new PromelaElementType("INLINE_CALL");
  IElementType IVAR = new PromelaElementType("IVAR");
  IElementType LABEL = new PromelaElementType("LABEL");
  IElementType LEN = new PromelaElementType("LEN");
  IElementType LEN_ANY_EXPRESSION = new PromelaElementType("LEN_ANY_EXPRESSION");
  IElementType LEN_BASE_EXPRESSION = new PromelaElementType("LEN_BASE_EXPRESSION");
  IElementType LTL = new PromelaElementType("LTL");
  IElementType LTL_BIN_BASE_EXPRESSION = new PromelaElementType("LTL_BIN_BASE_EXPRESSION");
  IElementType LTL_BODY = new PromelaElementType("LTL_BODY");
  IElementType LTL_UN_BASE_EXPRESSION = new PromelaElementType("LTL_UN_BASE_EXPRESSION");
  IElementType MODULES = new PromelaElementType("MODULES");
  IElementType MTYPE = new PromelaElementType("MTYPE");
  IElementType MTYPENAME = new PromelaElementType("MTYPENAME");
  IElementType MTYPE_VALUE = new PromelaElementType("MTYPE_VALUE");
  IElementType MULTIPLICATION_CONST_EXPRESSION = new PromelaElementType("MULTIPLICATION_CONST_EXPRESSION");
  IElementType NEVER = new PromelaElementType("NEVER");
  IElementType NO_TRACE = new PromelaElementType("NO_TRACE");
  IElementType NP_ANY_EXPRESSION = new PromelaElementType("NP_ANY_EXPRESSION");
  IElementType NP_BASE_EXPRESSION = new PromelaElementType("NP_BASE_EXPRESSION");
  IElementType NUMBER_RULE = new PromelaElementType("NUMBER_RULE");
  IElementType ONE_DECL = new PromelaElementType("ONE_DECL");
  IElementType OPTION = new PromelaElementType("OPTION");
  IElementType PARENTHESIS = new PromelaElementType("PARENTHESIS");
  IElementType PC_VALUE_ANY_EXPRESSION = new PromelaElementType("PC_VALUE_ANY_EXPRESSION");
  IElementType PC_VALUE_BASE_EXPRESSION = new PromelaElementType("PC_VALUE_BASE_EXPRESSION");
  IElementType PID_REFER_ANY_EXPRESSION = new PromelaElementType("PID_REFER_ANY_EXPRESSION");
  IElementType PID_REFER_BASE_EXPRESSION = new PromelaElementType("PID_REFER_BASE_EXPRESSION");
  IElementType POLL = new PromelaElementType("POLL");
  IElementType POLL_ANY_EXPRESSION = new PromelaElementType("POLL_ANY_EXPRESSION");
  IElementType POLL_BASE_EXPRESSION = new PromelaElementType("POLL_BASE_EXPRESSION");
  IElementType PRIORITY = new PromelaElementType("PRIORITY");
  IElementType PROCTYPE = new PromelaElementType("PROCTYPE");
  IElementType PROCTYPE_HEADER = new PromelaElementType("PROCTYPE_HEADER");
  IElementType PROPERTY_ACCESS = new PromelaElementType("PROPERTY_ACCESS");
  IElementType RANGE = new PromelaElementType("RANGE");
  IElementType RECEIVE = new PromelaElementType("RECEIVE");
  IElementType RECV_ARG = new PromelaElementType("RECV_ARG");
  IElementType RECV_ARGS = new PromelaElementType("RECV_ARGS");
  IElementType RUN = new PromelaElementType("RUN");
  IElementType RUN_ANY_EXPRESSION = new PromelaElementType("RUN_ANY_EXPRESSION");
  IElementType RUN_BASE_EXPRESSION = new PromelaElementType("RUN_BASE_EXPRESSION");
  IElementType SEND = new PromelaElementType("SEND");
  IElementType SEND_ARGS = new PromelaElementType("SEND_ARGS");
  IElementType SEQUENCE = new PromelaElementType("SEQUENCE");
  IElementType SEQUENCE_BLOCK = new PromelaElementType("SEQUENCE_BLOCK");
  IElementType SET_PRIORITY_ANY_EXPRESSION = new PromelaElementType("SET_PRIORITY_ANY_EXPRESSION");
  IElementType SET_PRIORITY_BASE_EXPRESSION = new PromelaElementType("SET_PRIORITY_BASE_EXPRESSION");
  IElementType SIZE_INITIALIZER = new PromelaElementType("SIZE_INITIALIZER");
  IElementType STEP = new PromelaElementType("STEP");
  IElementType STMNT = new PromelaElementType("STMNT");
  IElementType TERNARY_ANY_EXPRESSION = new PromelaElementType("TERNARY_ANY_EXPRESSION");
  IElementType TERNARY_BASE_EXPRESSION = new PromelaElementType("TERNARY_BASE_EXPRESSION");
  IElementType TIMEOUT_ANY_EXPRESSION = new PromelaElementType("TIMEOUT_ANY_EXPRESSION");
  IElementType TIMEOUT_BASE_EXPRESSION = new PromelaElementType("TIMEOUT_BASE_EXPRESSION");
  IElementType TRACE = new PromelaElementType("TRACE");
  IElementType TYPENAME = new PromelaElementType("TYPENAME");
  IElementType UNARY_ANY_EXPRESSION = new PromelaElementType("UNARY_ANY_EXPRESSION");
  IElementType UNARY_BASE_EXPRESSION = new PromelaElementType("UNARY_BASE_EXPRESSION");
  IElementType UNARY_CONST_EXPRESSION = new PromelaElementType("UNARY_CONST_EXPRESSION");
  IElementType UNIT_CONST_EXPRESSION = new PromelaElementType("UNIT_CONST_EXPRESSION");
  IElementType UNSIGNED_DECL = new PromelaElementType("UNSIGNED_DECL");
  IElementType UTYPE = new PromelaElementType("UTYPE");
  IElementType VAR = new PromelaElementType("VAR");
  IElementType VARREF = new PromelaElementType("VARREF");
  IElementType VARREF_ANY_EXPRESSION = new PromelaElementType("VARREF_ANY_EXPRESSION");
  IElementType VARREF_BASE_EXPRESSION = new PromelaElementType("VARREF_BASE_EXPRESSION");
  IElementType VISIBLE = new PromelaElementType("VISIBLE");
  IElementType WRAP_ANY_EXPRESSION = new PromelaElementType("WRAP_ANY_EXPRESSION");
  IElementType WRAP_BASE_EXPRESSION = new PromelaElementType("WRAP_BASE_EXPRESSION");

  IElementType ACTIVE_KW = new PromelaTokenType("active");
  IElementType ARROW = new PromelaTokenType("->");
  IElementType ASSERT_KW = new PromelaTokenType("assert");
  IElementType AT = new PromelaTokenType("@");
  IElementType ATOMIC_KW = new PromelaTokenType("atomic");
  IElementType BIT_TYPE_KW = new PromelaTokenType("bit");
  IElementType BLOCK_COMMENT = new PromelaTokenType("block_comment");
  IElementType BOOL_TYPE_KW = new PromelaTokenType("bool");
  IElementType BREAK_KW = new PromelaTokenType("break");
  IElementType BYTE_TYPE_KW = new PromelaTokenType("byte");
  IElementType CHANPOLL_EMPTY = new PromelaTokenType("empty");
  IElementType CHANPOLL_FULL = new PromelaTokenType("full");
  IElementType CHANPOLL_NEMPTY = new PromelaTokenType("nempty");
  IElementType CHANPOLL_NFULL = new PromelaTokenType("nfull");
  IElementType CHAN_TYPE_KW = new PromelaTokenType("chan");
  IElementType CLOSEBRACKET = new PromelaTokenType(")");
  IElementType CLOSECURLYBRACKET = new PromelaTokenType("}");
  IElementType CLOSESQUAREBRACKET = new PromelaTokenType("]");
  IElementType COL = new PromelaTokenType(":");
  IElementType COMMA = new PromelaTokenType(",");
  IElementType CONST_FALSE = new PromelaTokenType("false");
  IElementType CONST_SKIP = new PromelaTokenType("skip");
  IElementType CONST_TRUE = new PromelaTokenType("true");
  IElementType C_CODE = new PromelaTokenType(".*");
  IElementType C_CODE_KW = new PromelaTokenType("c_code");
  IElementType C_DECL_KW = new PromelaTokenType("c_decl");
  IElementType C_EXPR_KW = new PromelaTokenType("c_expr");
  IElementType C_STATE_KW = new PromelaTokenType("c_state");
  IElementType C_TRACK_KW = new PromelaTokenType("c_track");
  IElementType DISABLED_CODE = new PromelaTokenType("ignored code");
  IElementType DOT = new PromelaTokenType(".");
  IElementType DOTDOT = new PromelaTokenType("..");
  IElementType DO_END_KW = new PromelaTokenType("od");
  IElementType DO_KW = new PromelaTokenType("do");
  IElementType D_STEP_KW = new PromelaTokenType("d_step");
  IElementType ELSE_KW = new PromelaTokenType("else");
  IElementType ENABLED_KW = new PromelaTokenType("enabled");
  IElementType EOL = new PromelaTokenType("\\n");
  IElementType EOL_OR_SEMI = new PromelaTokenType("");
  IElementType EVAL_KW = new PromelaTokenType("eval");
  IElementType FOR_KW = new PromelaTokenType("for");
  IElementType GET_PRIORITY_KW = new PromelaTokenType("get_priority");
  IElementType GOTO_KW = new PromelaTokenType("goto");
  IElementType HIDDEN_KW = new PromelaTokenType("hidden");
  IElementType IF_END_KW = new PromelaTokenType("fi");
  IElementType IF_KW = new PromelaTokenType("if");
  IElementType INCDEC = new PromelaTokenType("incdec");
  IElementType INIT_KW = new PromelaTokenType("init");
  IElementType INLINE_KW = new PromelaTokenType("inline");
  IElementType INT_TYPE_KW = new PromelaTokenType("int");
  IElementType IN_KW = new PromelaTokenType("in");
  IElementType LEN_KW = new PromelaTokenType("len");
  IElementType LINE_COMMENT = new PromelaTokenType("line_comment");
  IElementType LOCAL_KW = new PromelaTokenType("local");
  IElementType LTL_KW = new PromelaTokenType("ltl");
  IElementType MTYPE_KW = new PromelaTokenType("mtype");
  IElementType NEVER_KW = new PromelaTokenType("never");
  IElementType NOTRACE_KW = new PromelaTokenType("notrace");
  IElementType NP_KW = new PromelaTokenType("np_");
  IElementType NUMBER = new PromelaTokenType("number");
  IElementType OF_KW = new PromelaTokenType("of");
  IElementType OPENBRACKET = new PromelaTokenType("(");
  IElementType OPENCURLYBRACKET = new PromelaTokenType("{");
  IElementType OPENSQUAREBRACKET = new PromelaTokenType("[");
  IElementType OP_AND = new PromelaTokenType("&&");
  IElementType OP_ASGN = new PromelaTokenType("=");
  IElementType OP_BAND = new PromelaTokenType("&");
  IElementType OP_BANG = new PromelaTokenType("!");
  IElementType OP_BOR = new PromelaTokenType("|");
  IElementType OP_DIV = new PromelaTokenType("/");
  IElementType OP_EQ = new PromelaTokenType("==");
  IElementType OP_GE = new PromelaTokenType(">=");
  IElementType OP_GT = new PromelaTokenType(">");
  IElementType OP_LE = new PromelaTokenType("<=");
  IElementType OP_LT = new PromelaTokenType("<");
  IElementType OP_MINUS = new PromelaTokenType("-");
  IElementType OP_MOD = new PromelaTokenType("%");
  IElementType OP_NEG = new PromelaTokenType("~");
  IElementType OP_NEQ = new PromelaTokenType("!=");
  IElementType OP_OR = new PromelaTokenType("||");
  IElementType OP_PLUS = new PromelaTokenType("+");
  IElementType OP_SLEFT = new PromelaTokenType("<<");
  IElementType OP_SRIGHT = new PromelaTokenType(">>");
  IElementType OP_STAR = new PromelaTokenType("*");
  IElementType OP_XOR = new PromelaTokenType("^");
  IElementType PC_VALUE_KW = new PromelaTokenType("pc_value");
  IElementType PREPROCESSOR_COMMENT = new PromelaTokenType("preprocessor_comment");
  IElementType PRINTF_KW = new PromelaTokenType("printf");
  IElementType PRINTM_KW = new PromelaTokenType("printm");
  IElementType PRIORITY_KW = new PromelaTokenType("priority");
  IElementType PROCTYPE_D_KW = new PromelaTokenType("D_proctype");
  IElementType PROCTYPE_KW = new PromelaTokenType("proctype");
  IElementType PROVIDED_KW = new PromelaTokenType("provided");
  IElementType QMARK = new PromelaTokenType("?");
  IElementType RETURN_KW = new PromelaTokenType("return");
  IElementType RUN_KW = new PromelaTokenType("run");
  IElementType SELECT_KW = new PromelaTokenType("select");
  IElementType SEMI = new PromelaTokenType(";");
  IElementType SEP = new PromelaTokenType("::");
  IElementType SET_PRIORITY_KW = new PromelaTokenType("set_priority");
  IElementType SHORT_TYPE_KW = new PromelaTokenType("short");
  IElementType SHOW_KW = new PromelaTokenType("show");
  IElementType STRING = new PromelaTokenType("string");
  IElementType TIMEOUT_KW = new PromelaTokenType("timeout");
  IElementType TRACE_KW = new PromelaTokenType("trace");
  IElementType TYPEDEF_KW = new PromelaTokenType("typedef");
  IElementType UNAME = new PromelaTokenType("uname");
  IElementType UNLESS_KW = new PromelaTokenType("unless");
  IElementType UNSIGNED_KW = new PromelaTokenType("unsigned");
  IElementType XR_KW = new PromelaTokenType("xr");
  IElementType XS_KW = new PromelaTokenType("xs");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ACTIVE) {
        return new PromelaActiveImpl(node);
      }
      else if (type == ACTIVE_PRIORITY) {
        return new PromelaActivePriorityImpl(node);
      }
      else if (type == ADDITION_CONST_EXPRESSION) {
        return new PromelaAdditionConstExpressionImpl(node);
      }
      else if (type == ARG_LST) {
        return new PromelaArgLstImpl(node);
      }
      else if (type == ARRAY_INIT) {
        return new PromelaArrayInitImpl(node);
      }
      else if (type == ASSIGN) {
        return new PromelaAssignImpl(node);
      }
      else if (type == ATOMIC_STMNT) {
        return new PromelaAtomicStmntImpl(node);
      }
      else if (type == BINARY_ANY_EXPRESSION) {
        return new PromelaBinaryAnyExpressionImpl(node);
      }
      else if (type == BINARY_BASE_EXPRESSION) {
        return new PromelaBinaryBaseExpressionImpl(node);
      }
      else if (type == CEXPR_ANY_EXPRESSION) {
        return new PromelaCexprAnyExpressionImpl(node);
      }
      else if (type == CHANPOLL) {
        return new PromelaChanpollImpl(node);
      }
      else if (type == CHANPOLL_BASE_EXPRESSION) {
        return new PromelaChanpollBaseExpressionImpl(node);
      }
      else if (type == CH_INIT) {
        return new PromelaChInitImpl(node);
      }
      else if (type == CH_INIT_TYPES) {
        return new PromelaChInitTypesImpl(node);
      }
      else if (type == CONST) {
        return new PromelaConstImpl(node);
      }
      else if (type == CONST_ANY_EXPRESSION) {
        return new PromelaConstAnyExpressionImpl(node);
      }
      else if (type == CONST_BASE_EXPRESSION) {
        return new PromelaConstBaseExpressionImpl(node);
      }
      else if (type == CUSTOM_TYPENAME) {
        return new PromelaCustomTypenameImpl(node);
      }
      else if (type == DO_STMNT) {
        return new PromelaDoStmntImpl(node);
      }
      else if (type == D_PROCTYPE) {
        return new PromelaDProctypeImpl(node);
      }
      else if (type == ENABLED_ANY_EXPRESSION) {
        return new PromelaEnabledAnyExpressionImpl(node);
      }
      else if (type == ENABLED_BASE_EXPRESSION) {
        return new PromelaEnabledBaseExpressionImpl(node);
      }
      else if (type == ENABLER) {
        return new PromelaEnablerImpl(node);
      }
      else if (type == FOR_STMNT) {
        return new PromelaForStmntImpl(node);
      }
      else if (type == GET_PRIORITY_ANY_EXPRESSION) {
        return new PromelaGetPriorityAnyExpressionImpl(node);
      }
      else if (type == GET_PRIORITY_BASE_EXPRESSION) {
        return new PromelaGetPriorityBaseExpressionImpl(node);
      }
      else if (type == GOTO) {
        return new PromelaGotoImpl(node);
      }
      else if (type == IDENTIFIER) {
        return new PromelaIdentifierImpl(node);
      }
      else if (type == IF_STMNT) {
        return new PromelaIfStmntImpl(node);
      }
      else if (type == INDEXER) {
        return new PromelaIndexerImpl(node);
      }
      else if (type == INIT) {
        return new PromelaInitImpl(node);
      }
      else if (type == INLINE) {
        return new PromelaInlineImpl(node);
      }
      else if (type == INLINE_ARG) {
        return new PromelaInlineArgImpl(node);
      }
      else if (type == INLINE_CALL) {
        return new PromelaInlineCallImpl(node);
      }
      else if (type == IVAR) {
        return new PromelaIvarImpl(node);
      }
      else if (type == LABEL) {
        return new PromelaLabelImpl(node);
      }
      else if (type == LEN) {
        return new PromelaLenImpl(node);
      }
      else if (type == LEN_ANY_EXPRESSION) {
        return new PromelaLenAnyExpressionImpl(node);
      }
      else if (type == LEN_BASE_EXPRESSION) {
        return new PromelaLenBaseExpressionImpl(node);
      }
      else if (type == LTL) {
        return new PromelaLtlImpl(node);
      }
      else if (type == LTL_BIN_BASE_EXPRESSION) {
        return new PromelaLtlBinBaseExpressionImpl(node);
      }
      else if (type == LTL_BODY) {
        return new PromelaLtlBodyImpl(node);
      }
      else if (type == LTL_UN_BASE_EXPRESSION) {
        return new PromelaLtlUnBaseExpressionImpl(node);
      }
      else if (type == MODULES) {
        return new PromelaModulesImpl(node);
      }
      else if (type == MTYPE) {
        return new PromelaMtypeImpl(node);
      }
      else if (type == MTYPENAME) {
        return new PromelaMtypenameImpl(node);
      }
      else if (type == MTYPE_VALUE) {
        return new PromelaMtypeValueImpl(node);
      }
      else if (type == MULTIPLICATION_CONST_EXPRESSION) {
        return new PromelaMultiplicationConstExpressionImpl(node);
      }
      else if (type == NEVER) {
        return new PromelaNeverImpl(node);
      }
      else if (type == NO_TRACE) {
        return new PromelaNoTraceImpl(node);
      }
      else if (type == NP_ANY_EXPRESSION) {
        return new PromelaNpAnyExpressionImpl(node);
      }
      else if (type == NP_BASE_EXPRESSION) {
        return new PromelaNpBaseExpressionImpl(node);
      }
      else if (type == NUMBER_RULE) {
        return new PromelaNumberRuleImpl(node);
      }
      else if (type == ONE_DECL) {
        return new PromelaOneDeclImpl(node);
      }
      else if (type == OPTION) {
        return new PromelaOptionImpl(node);
      }
      else if (type == PARENTHESIS) {
        return new PromelaParenthesisImpl(node);
      }
      else if (type == PC_VALUE_ANY_EXPRESSION) {
        return new PromelaPcValueAnyExpressionImpl(node);
      }
      else if (type == PC_VALUE_BASE_EXPRESSION) {
        return new PromelaPcValueBaseExpressionImpl(node);
      }
      else if (type == PID_REFER_ANY_EXPRESSION) {
        return new PromelaPidReferAnyExpressionImpl(node);
      }
      else if (type == PID_REFER_BASE_EXPRESSION) {
        return new PromelaPidReferBaseExpressionImpl(node);
      }
      else if (type == POLL) {
        return new PromelaPollImpl(node);
      }
      else if (type == POLL_ANY_EXPRESSION) {
        return new PromelaPollAnyExpressionImpl(node);
      }
      else if (type == POLL_BASE_EXPRESSION) {
        return new PromelaPollBaseExpressionImpl(node);
      }
      else if (type == PRIORITY) {
        return new PromelaPriorityImpl(node);
      }
      else if (type == PROCTYPE) {
        return new PromelaProctypeImpl(node);
      }
      else if (type == PROCTYPE_HEADER) {
        return new PromelaProctypeHeaderImpl(node);
      }
      else if (type == PROPERTY_ACCESS) {
        return new PromelaPropertyAccessImpl(node);
      }
      else if (type == RANGE) {
        return new PromelaRangeImpl(node);
      }
      else if (type == RECEIVE) {
        return new PromelaReceiveImpl(node);
      }
      else if (type == RECV_ARG) {
        return new PromelaRecvArgImpl(node);
      }
      else if (type == RECV_ARGS) {
        return new PromelaRecvArgsImpl(node);
      }
      else if (type == RUN) {
        return new PromelaRunImpl(node);
      }
      else if (type == RUN_ANY_EXPRESSION) {
        return new PromelaRunAnyExpressionImpl(node);
      }
      else if (type == RUN_BASE_EXPRESSION) {
        return new PromelaRunBaseExpressionImpl(node);
      }
      else if (type == SEND) {
        return new PromelaSendImpl(node);
      }
      else if (type == SEND_ARGS) {
        return new PromelaSendArgsImpl(node);
      }
      else if (type == SEQUENCE) {
        return new PromelaSequenceImpl(node);
      }
      else if (type == SEQUENCE_BLOCK) {
        return new PromelaSequenceBlockImpl(node);
      }
      else if (type == SET_PRIORITY_ANY_EXPRESSION) {
        return new PromelaSetPriorityAnyExpressionImpl(node);
      }
      else if (type == SET_PRIORITY_BASE_EXPRESSION) {
        return new PromelaSetPriorityBaseExpressionImpl(node);
      }
      else if (type == SIZE_INITIALIZER) {
        return new PromelaSizeInitializerImpl(node);
      }
      else if (type == STEP) {
        return new PromelaStepImpl(node);
      }
      else if (type == STMNT) {
        return new PromelaStmntImpl(node);
      }
      else if (type == TERNARY_ANY_EXPRESSION) {
        return new PromelaTernaryAnyExpressionImpl(node);
      }
      else if (type == TERNARY_BASE_EXPRESSION) {
        return new PromelaTernaryBaseExpressionImpl(node);
      }
      else if (type == TIMEOUT_ANY_EXPRESSION) {
        return new PromelaTimeoutAnyExpressionImpl(node);
      }
      else if (type == TIMEOUT_BASE_EXPRESSION) {
        return new PromelaTimeoutBaseExpressionImpl(node);
      }
      else if (type == TRACE) {
        return new PromelaTraceImpl(node);
      }
      else if (type == TYPENAME) {
        return new PromelaTypenameImpl(node);
      }
      else if (type == UNARY_ANY_EXPRESSION) {
        return new PromelaUnaryAnyExpressionImpl(node);
      }
      else if (type == UNARY_BASE_EXPRESSION) {
        return new PromelaUnaryBaseExpressionImpl(node);
      }
      else if (type == UNARY_CONST_EXPRESSION) {
        return new PromelaUnaryConstExpressionImpl(node);
      }
      else if (type == UNIT_CONST_EXPRESSION) {
        return new PromelaUnitConstExpressionImpl(node);
      }
      else if (type == UNSIGNED_DECL) {
        return new PromelaUnsignedDeclImpl(node);
      }
      else if (type == UTYPE) {
        return new PromelaUtypeImpl(node);
      }
      else if (type == VAR) {
        return new PromelaVarImpl(node);
      }
      else if (type == VARREF) {
        return new PromelaVarrefImpl(node);
      }
      else if (type == VARREF_ANY_EXPRESSION) {
        return new PromelaVarrefAnyExpressionImpl(node);
      }
      else if (type == VARREF_BASE_EXPRESSION) {
        return new PromelaVarrefBaseExpressionImpl(node);
      }
      else if (type == VISIBLE) {
        return new PromelaVisibleImpl(node);
      }
      else if (type == WRAP_ANY_EXPRESSION) {
        return new PromelaWrapAnyExpressionImpl(node);
      }
      else if (type == WRAP_BASE_EXPRESSION) {
        return new PromelaWrapBaseExpressionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
