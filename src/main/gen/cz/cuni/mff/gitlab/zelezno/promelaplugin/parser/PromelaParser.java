// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.parser.PromelaParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PromelaParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return root(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ADDITION_CONST_EXPRESSION, CONST_EXPRESSION, MULTIPLICATION_CONST_EXPRESSION, UNARY_CONST_EXPRESSION,
      UNIT_CONST_EXPRESSION),
    create_token_set_(ANY_EXPRESSION, BINARY_ANY_EXPRESSION, CEXPR_ANY_EXPRESSION, CONST_ANY_EXPRESSION,
      ENABLED_ANY_EXPRESSION, GET_PRIORITY_ANY_EXPRESSION, LEN_ANY_EXPRESSION, NP_ANY_EXPRESSION,
      PC_VALUE_ANY_EXPRESSION, PID_REFER_ANY_EXPRESSION, POLL_ANY_EXPRESSION, RUN_ANY_EXPRESSION,
      SET_PRIORITY_ANY_EXPRESSION, TERNARY_ANY_EXPRESSION, TIMEOUT_ANY_EXPRESSION, UNARY_ANY_EXPRESSION,
      VARREF_ANY_EXPRESSION, WRAP_ANY_EXPRESSION),
    create_token_set_(BASE_EXPRESSION, BINARY_BASE_EXPRESSION, CHANPOLL_BASE_EXPRESSION, CONST_BASE_EXPRESSION,
      ENABLED_BASE_EXPRESSION, GET_PRIORITY_BASE_EXPRESSION, LEN_BASE_EXPRESSION, LTL_BIN_BASE_EXPRESSION,
      LTL_UN_BASE_EXPRESSION, NP_BASE_EXPRESSION, PC_VALUE_BASE_EXPRESSION, PID_REFER_BASE_EXPRESSION,
      POLL_BASE_EXPRESSION, RUN_BASE_EXPRESSION, SET_PRIORITY_BASE_EXPRESSION, TERNARY_BASE_EXPRESSION,
      TIMEOUT_BASE_EXPRESSION, UNARY_BASE_EXPRESSION, VARREF_BASE_EXPRESSION, WRAP_BASE_EXPRESSION),
  };

  /* ********************************************************** */
  // 'active' [ active_priority ]
  public static boolean active(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ACTIVE, "<active>");
    r = consumeToken(b, ACTIVE_KW);
    p = r; // pin = 1
    r = r && active_1(b, l + 1);
    exit_section_(b, l, m, r, p, PromelaParser::active_r);
    return r || p;
  }

  // [ active_priority ]
  private static boolean active_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active_1")) return false;
    active_priority(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' active_value ']'
  public static boolean active_priority(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active_priority")) return false;
    if (!nextTokenIs(b, OPENSQUAREBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ACTIVE_PRIORITY, null);
    r = consumeToken(b, OPENSQUAREBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, active_value(b, l + 1));
    r = p && consumeToken(b, CLOSESQUAREBRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // active proctype_type
  static boolean active_proctype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active_proctype")) return false;
    if (!nextTokenIs(b, ACTIVE_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = active(b, l + 1);
    p = r; // pin = 1
    r = r && proctype_type(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !proctype_type
  static boolean active_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !proctype_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // const_expression
  static boolean active_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::active_value_r);
    return r;
  }

  /* ********************************************************** */
  // !']'
  static boolean active_value_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "active_value_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (multiplication_const_expression <<consumeAdditiveOperation>> addition_const_expression) | multiplication_const_expression
  public static boolean addition_const_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addition_const_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, ADDITION_CONST_EXPRESSION, "<addition const expression>");
    r = addition_const_expression_0(b, l + 1);
    if (!r) r = multiplication_const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // multiplication_const_expression <<consumeAdditiveOperation>> addition_const_expression
  private static boolean addition_const_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "addition_const_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = multiplication_const_expression(b, l + 1);
    r = r && consumeAdditiveOperation(b, l + 1);
    r = r && addition_const_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // any_expression ( ',' any_expression ) *
  public static boolean arg_lst(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_lst")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARG_LST, "<arg lst>");
    r = any_expression(b, l + 1, -1);
    r = r && arg_lst_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' any_expression ) *
  private static boolean arg_lst_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_lst_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!arg_lst_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "arg_lst_1", c)) break;
    }
    return true;
  }

  // ',' any_expression
  private static boolean arg_lst_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arg_lst_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "{" constant_list "}"
  public static boolean array_init(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_init")) return false;
    if (!nextTokenIs(b, OPENCURLYBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ARRAY_INIT, null);
    r = consumeToken(b, OPENCURLYBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, constant_list(b, l + 1));
    r = p && consumeToken(b, CLOSECURLYBRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'assert' base_expression
  static boolean assert_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_$")) return false;
    if (!nextTokenIs(b, ASSERT_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ASSERT_KW);
    r = r && base_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // varref '=' base_expression /* standard assignment */
  // 	| varref incdec	/* increment/decrement */ | inline_call
  public static boolean assign(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN, "<assign>");
    r = assign_0(b, l + 1);
    if (!r) r = assign_1(b, l + 1);
    if (!r) r = inline_call(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // varref '=' base_expression
  private static boolean assign_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeToken(b, OP_ASGN);
    r = r && base_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // varref incdec
  private static boolean assign_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeToken(b, INCDEC);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'atomic' sequence_block
  public static boolean atomic_stmnt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atomic_stmnt")) return false;
    if (!nextTokenIs(b, ATOMIC_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ATOMIC_STMNT, null);
    r = consumeToken(b, ATOMIC_KW);
    p = r; // pin = 1
    r = r && sequence_block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // <<consumeBinOperation>>
  static boolean binarop(PsiBuilder b, int l) {
    return consumeBinOperation(b, l + 1);
  }

  /* ********************************************************** */
  // cstate | ccode
  static boolean c_fcts(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "c_fcts")) return false;
    boolean r;
    r = cstate(b, l + 1);
    if (!r) r = ccode(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ('c_code' | 'c_decl') ccode_body
  static boolean ccode(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode")) return false;
    if (!nextTokenIs(b, "", C_CODE_KW, C_DECL_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = ccode_0(b, l + 1);
    p = r; // pin = 1
    r = r && ccode_body(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'c_code' | 'c_decl'
  private static boolean ccode_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode_0")) return false;
    boolean r;
    r = consumeToken(b, C_CODE_KW);
    if (!r) r = consumeToken(b, C_DECL_KW);
    return r;
  }

  /* ********************************************************** */
  // ('[' c_code* ']')? '{' c_code* '}'
  static boolean ccode_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode_body")) return false;
    if (!nextTokenIs(b, "", OPENCURLYBRACKET, OPENSQUAREBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ccode_body_0(b, l + 1);
    r = r && consumeToken(b, OPENCURLYBRACKET);
    r = r && ccode_body_2(b, l + 1);
    r = r && consumeToken(b, CLOSECURLYBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('[' c_code* ']')?
  private static boolean ccode_body_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode_body_0")) return false;
    ccode_body_0_0(b, l + 1);
    return true;
  }

  // '[' c_code* ']'
  private static boolean ccode_body_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode_body_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPENSQUAREBRACKET);
    r = r && ccode_body_0_0_1(b, l + 1);
    r = r && consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // c_code*
  private static boolean ccode_body_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode_body_0_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, C_CODE)) break;
      if (!empty_element_parsed_guard_(b, "ccode_body_0_0_1", c)) break;
    }
    return true;
  }

  // c_code*
  private static boolean ccode_body_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ccode_body_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, C_CODE)) break;
      if (!empty_element_parsed_guard_(b, "ccode_body_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '[' const_expression ']' 'of' '{' ch_init_types close_curly_bracket
  public static boolean ch_init(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ch_init")) return false;
    if (!nextTokenIs(b, OPENSQUAREBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CH_INIT, null);
    r = consumeToken(b, OPENSQUAREBRACKET);
    r = r && const_expression(b, l + 1);
    r = r && consumeTokens(b, 2, CLOSESQUAREBRACKET, OF_KW, OPENCURLYBRACKET);
    p = r; // pin = 4
    r = r && report_error_(b, ch_init_types(b, l + 1));
    r = p && close_curly_bracket(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // typename ( ',' typename ) *
  public static boolean ch_init_types(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ch_init_types")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CH_INIT_TYPES, "<ch init types>");
    r = typename(b, l + 1);
    r = r && ch_init_types_1(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::ch_init_types_r);
    return r;
  }

  // ( ',' typename ) *
  private static boolean ch_init_types_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ch_init_types_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ch_init_types_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ch_init_types_1", c)) break;
    }
    return true;
  }

  // ',' typename
  private static boolean ch_init_types_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ch_init_types_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && typename(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !close_curly_bracket
  static boolean ch_init_types_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ch_init_types_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !close_curly_bracket(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'full' | 'empty' | 'nfull' | 'nempty'
  public static boolean chanpoll(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chanpoll")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CHANPOLL, "<chanpoll>");
    r = consumeToken(b, CHANPOLL_FULL);
    if (!r) r = consumeToken(b, CHANPOLL_EMPTY);
    if (!r) r = consumeToken(b, CHANPOLL_NFULL);
    if (!r) r = consumeToken(b, CHANPOLL_NEMPTY);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ((semi | arrow) '}') | '}'
  static boolean close_curly_bracket(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "close_curly_bracket")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = close_curly_bracket_0(b, l + 1);
    if (!r) r = consumeToken(b, CLOSECURLYBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // (semi | arrow) '}'
  private static boolean close_curly_bracket_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "close_curly_bracket_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = close_curly_bracket_0_0(b, l + 1);
    r = r && consumeToken(b, CLOSECURLYBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi | arrow
  private static boolean close_curly_bracket_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "close_curly_bracket_0_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, ARROW);
    return r;
  }

  /* ********************************************************** */
  // 'true' | 'false' | 'skip' | number_rule
  public static boolean const_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST, "<const>");
    r = consumeToken(b, CONST_TRUE);
    if (!r) r = consumeToken(b, CONST_FALSE);
    if (!r) r = consumeToken(b, CONST_SKIP);
    if (!r) r = number_rule(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // addition_const_expression
  public static boolean const_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, CONST_EXPRESSION, "<const expression>");
    r = addition_const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // const (',' const)*
  static boolean constant_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = const_$(b, l + 1);
    r = r && constant_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::constant_list_recover);
    return r;
  }

  // (',' const)*
  private static boolean constant_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constant_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "constant_list_1", c)) break;
    }
    return true;
  }

  // ',' const
  private static boolean constant_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && const_$(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !('}' | ';')
  static boolean constant_list_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_list_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !constant_list_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '}' | ';'
  private static boolean constant_list_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_list_recover_0")) return false;
    boolean r;
    r = consumeToken(b, CLOSECURLYBRACKET);
    if (!r) r = consumeToken(b, SEMI);
    return r;
  }

  /* ********************************************************** */
  // ('c_state' | 'c_track') string string string?
  static boolean cstate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cstate")) return false;
    if (!nextTokenIs(b, "", C_STATE_KW, C_TRACK_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = cstate_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeTokens(b, -1, STRING, STRING));
    r = p && cstate_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'c_state' | 'c_track'
  private static boolean cstate_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cstate_0")) return false;
    boolean r;
    r = consumeToken(b, C_STATE_KW);
    if (!r) r = consumeToken(b, C_TRACK_KW);
    return r;
  }

  // string?
  private static boolean cstate_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cstate_3")) return false;
    consumeToken(b, STRING);
    return true;
  }

  /* ********************************************************** */
  // <<consumeType>>
  public static boolean custom_typename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "custom_typename")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CUSTOM_TYPENAME, "<custom typename>");
    r = consumeType(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'D_proctype'
  public static boolean d_proctype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "d_proctype")) return false;
    if (!nextTokenIs(b, PROCTYPE_D_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PROCTYPE_D_KW);
    exit_section_(b, m, D_PROCTYPE, r);
    return r;
  }

  /* ********************************************************** */
  // 'd_step' sequence_block
  static boolean d_step(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "d_step")) return false;
    if (!nextTokenIs(b, D_STEP_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, D_STEP_KW);
    p = r; // pin = 1
    r = r && sequence_block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // one_decl ( semicolon_rule one_decl ) *
  static boolean decl_lst(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "decl_lst")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = one_decl(b, l + 1);
    r = r && decl_lst_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( semicolon_rule one_decl ) *
  private static boolean decl_lst_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "decl_lst_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!decl_lst_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "decl_lst_1", c)) break;
    }
    return true;
  }

  // semicolon_rule one_decl
  private static boolean decl_lst_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "decl_lst_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_rule(b, l + 1);
    r = r && one_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // options
  static boolean do_options(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_options")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = options(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::do_options_r);
    return r;
  }

  /* ********************************************************** */
  // !('od')
  static boolean do_options_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_options_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, DO_END_KW);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'do' do_options 'od'
  public static boolean do_stmnt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_stmnt")) return false;
    if (!nextTokenIs(b, DO_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DO_STMNT, null);
    r = consumeToken(b, DO_KW);
    p = r; // pin = 1
    r = r && report_error_(b, do_options(b, l + 1));
    r = p && consumeToken(b, DO_END_KW) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'provided' <<parenthesis base_expression>>
  public static boolean enabler(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enabler")) return false;
    if (!nextTokenIs(b, PROVIDED_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENABLER, null);
    r = consumeToken(b, PROVIDED_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, base_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'for' <<parenthesis range >> semi? sequence_block
  public static boolean for_stmnt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_stmnt")) return false;
    if (!nextTokenIs(b, FOR_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FOR_STMNT, null);
    r = consumeToken(b, FOR_KW);
    p = r; // pin = 1
    r = r && report_error_(b, parenthesis(b, l + 1, PromelaParser::range));
    r = p && report_error_(b, for_stmnt_2(b, l + 1)) && r;
    r = p && sequence_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semi?
  private static boolean for_stmnt_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_stmnt_2")) return false;
    consumeToken(b, SEMI);
    return true;
  }

  /* ********************************************************** */
  // 'goto' identifier
  public static boolean goto_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "goto_$")) return false;
    if (!nextTokenIs(b, GOTO_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GOTO, null);
    r = consumeToken(b, GOTO_KW);
    p = r; // pin = 1
    r = r && identifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // <<consumeNonSpecialIdentifier>>
  public static boolean identifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "identifier")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IDENTIFIER, "<identifier>");
    r = consumeNonSpecialIdentifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // options
  static boolean if_options(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_options")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = options(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::if_options_r);
    return r;
  }

  /* ********************************************************** */
  // !('fi')
  static boolean if_options_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_options_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, IF_END_KW);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'if' if_options 'fi'
  public static boolean if_stmnt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmnt")) return false;
    if (!nextTokenIs(b, IF_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IF_STMNT, null);
    r = consumeToken(b, IF_KW);
    p = r; // pin = 1
    r = r && report_error_(b, if_options(b, l + 1));
    r = p && consumeToken(b, IF_END_KW) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '[' any_expression ']'
  public static boolean indexer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "indexer")) return false;
    if (!nextTokenIs(b, OPENSQUAREBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPENSQUAREBRACKET);
    r = r && any_expression(b, l + 1, -1);
    r = r && consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, m, INDEXER, r);
    return r;
  }

  /* ********************************************************** */
  // init_header sequence_block
  public static boolean init(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "init")) return false;
    if (!nextTokenIs(b, INIT_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INIT, null);
    r = init_header(b, l + 1);
    p = r; // pin = 1
    r = r && sequence_block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'init' [ priority ]
  static boolean init_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "init_header")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, INIT_KW);
    p = r; // pin = 1
    r = r && init_header_1(b, l + 1);
    exit_section_(b, l, m, r, p, PromelaParser::init_header_r);
    return r || p;
  }

  // [ priority ]
  private static boolean init_header_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "init_header_1")) return false;
    priority(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !('{')
  static boolean init_header_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "init_header_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, OPENCURLYBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'inline' <<captureUname>> identifier inline_args sequence_block
  public static boolean inline(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline")) return false;
    if (!nextTokenIs(b, INLINE_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INLINE, null);
    r = consumeToken(b, INLINE_KW);
    p = r; // pin = 1
    r = r && report_error_(b, captureUname(b, l + 1));
    r = p && report_error_(b, identifier(b, l + 1)) && r;
    r = p && report_error_(b, inline_args(b, l + 1)) && r;
    r = p && sequence_block(b, l + 1) && r;
    register_hook_(b, INLINE_ADDED, null);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier
  public static boolean inline_arg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_arg")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INLINE_ARG, "<inline arg>");
    r = identifier(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::inline_arg_r);
    return r;
  }

  /* ********************************************************** */
  // !(')'|','| '}')
  static boolean inline_arg_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_arg_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !inline_arg_r_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ')'|','| '}'
  private static boolean inline_arg_r_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_arg_r_0")) return false;
    boolean r;
    r = consumeToken(b, CLOSEBRACKET);
    if (!r) r = consumeToken(b, COMMA);
    if (!r) r = consumeToken(b, CLOSECURLYBRACKET);
    return r;
  }

  /* ********************************************************** */
  // <<parenthesis inline_args_par>>
  static boolean inline_args(PsiBuilder b, int l) {
    return parenthesis(b, l + 1, PromelaParser::inline_args_par);
  }

  /* ********************************************************** */
  // [inline_arg ( ',' inline_arg ) *]
  static boolean inline_args_par(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_args_par")) return false;
    inline_args_par_0(b, l + 1);
    return true;
  }

  // inline_arg ( ',' inline_arg ) *
  private static boolean inline_args_par_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_args_par_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = inline_arg(b, l + 1);
    r = r && inline_args_par_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' inline_arg ) *
  private static boolean inline_args_par_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_args_par_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!inline_args_par_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "inline_args_par_0_1", c)) break;
    }
    return true;
  }

  // ',' inline_arg
  private static boolean inline_args_par_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_args_par_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && inline_arg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<eolOrSemiAsEOL>> <<consumeInline>> '(' [ arg_lst ] ')'
  public static boolean inline_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_call")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, INLINE_CALL, "<inline call>");
    r = eolOrSemiAsEOL(b, l + 1);
    r = r && consumeInline(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, OPENBRACKET));
    r = p && report_error_(b, inline_call_3(b, l + 1)) && r;
    r = p && consumeToken(b, CLOSEBRACKET) && r;
    register_hook_(b, POP_REMAPPER, null);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ arg_lst ]
  private static boolean inline_call_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "inline_call_3")) return false;
    arg_lst(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // identifier size_initializer? ('=' array_init | '=' any_expression | '=' ch_init)?
  public static boolean ivar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IVAR, "<ivar>");
    r = identifier(b, l + 1);
    r = r && ivar_1(b, l + 1);
    r = r && ivar_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // size_initializer?
  private static boolean ivar_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar_1")) return false;
    size_initializer(b, l + 1);
    return true;
  }

  // ('=' array_init | '=' any_expression | '=' ch_init)?
  private static boolean ivar_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar_2")) return false;
    ivar_2_0(b, l + 1);
    return true;
  }

  // '=' array_init | '=' any_expression | '=' ch_init
  private static boolean ivar_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ivar_2_0_0(b, l + 1);
    if (!r) r = ivar_2_0_1(b, l + 1);
    if (!r) r = ivar_2_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' array_init
  private static boolean ivar_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASGN);
    r = r && array_init(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' any_expression
  private static boolean ivar_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar_2_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASGN);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' ch_init
  private static boolean ivar_2_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ivar_2_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASGN);
    r = r && ch_init(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier ':'
  public static boolean label(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LABEL, "<label>");
    r = identifier(b, l + 1);
    r = r && consumeToken(b, COL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'len' <<parenthesis varref>>
  public static boolean len(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "len")) return false;
    if (!nextTokenIs(b, LEN_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LEN, null);
    r = consumeToken(b, LEN_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::varref);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // "ltl" <<enableLTL>> identifier? ltl_body <<disableLTL>>
  public static boolean ltl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl")) return false;
    if (!nextTokenIs(b, LTL_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LTL, null);
    r = consumeToken(b, LTL_KW);
    p = r; // pin = 1
    r = r && report_error_(b, enableLTL(b, l + 1));
    r = p && report_error_(b, ltl_2(b, l + 1)) && r;
    r = p && report_error_(b, ltl_body(b, l + 1)) && r;
    r = p && disableLTL(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // identifier?
  private static boolean ltl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_2")) return false;
    identifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '->'
  //  | <<consumeTokenPair 'OP_LT' 'ARROW'>>  // <->
  //  | <<consumeLTLBinaryOp>>
  static boolean ltl_binop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_binop")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARROW);
    if (!r) r = consumeTokenPair(b, l + 1, OP_LT, ARROW);
    if (!r) r = consumeLTLBinaryOp(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{'  <<enableLTL>> base_expression semicolon_rule?  <<disableLTL>> '}'
  public static boolean ltl_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_body")) return false;
    if (!nextTokenIs(b, OPENCURLYBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LTL_BODY, null);
    r = consumeToken(b, OPENCURLYBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, enableLTL(b, l + 1));
    r = p && report_error_(b, base_expression(b, l + 1, -1)) && r;
    r = p && report_error_(b, ltl_body_3(b, l + 1)) && r;
    r = p && report_error_(b, disableLTL(b, l + 1)) && r;
    r = p && consumeToken(b, CLOSECURLYBRACKET) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semicolon_rule?
  private static boolean ltl_body_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_body_3")) return false;
    semicolon_rule(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<consumeTokenPair 'OP_LT' 'OP_GT'>> // <>
  //     | <<consumeTokenPair 'OPENSQUAREBRACKET' 'CLOSESQUAREBRACKET'>> // []
  //     | <<consumeLTLUnaryOps>>
  static boolean ltl_unop(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_unop")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenPair(b, l + 1, OP_LT, OP_GT);
    if (!r) r = consumeTokenPair(b, l + 1, OPENSQUAREBRACKET, CLOSESQUAREBRACKET);
    if (!r) r = consumeLTLUnaryOps(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // proctype
  //     | init
  //     | never
  //     | ltl
  //     | trace
  //     | utype
  //     | mtype
  //     | one_decl
  //     | inline
  //     | c_fcts
  static boolean module_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_$")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = proctype(b, l + 1);
    if (!r) r = init(b, l + 1);
    if (!r) r = never(b, l + 1);
    if (!r) r = ltl(b, l + 1);
    if (!r) r = trace(b, l + 1);
    if (!r) r = utype(b, l + 1);
    if (!r) r = mtype(b, l + 1);
    if (!r) r = one_decl(b, l + 1);
    if (!r) r = inline(b, l + 1);
    if (!r) r = c_fcts(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (module | semicolon_or_arrow)+
  public static boolean modules(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modules")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODULES, "<modules>");
    r = modules_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!modules_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "modules", c)) break;
    }
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // module | semicolon_or_arrow
  private static boolean modules_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "modules_0")) return false;
    boolean r;
    r = module_$(b, l + 1);
    if (!r) r = semicolon_or_arrow(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'mtype'  [( mtype_subtype '=' | '=' )] '{' mtype_value  (',' mtype_value)* close_curly_bracket
  public static boolean mtype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype")) return false;
    if (!nextTokenIs(b, MTYPE_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MTYPE_KW);
    r = r && mtype_1(b, l + 1);
    r = r && consumeToken(b, OPENCURLYBRACKET);
    r = r && mtype_value(b, l + 1);
    r = r && mtype_4(b, l + 1);
    r = r && close_curly_bracket(b, l + 1);
    exit_section_(b, m, MTYPE, r);
    return r;
  }

  // [( mtype_subtype '=' | '=' )]
  private static boolean mtype_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_1")) return false;
    mtype_1_0(b, l + 1);
    return true;
  }

  // mtype_subtype '=' | '='
  private static boolean mtype_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mtype_1_0_0(b, l + 1);
    if (!r) r = consumeToken(b, OP_ASGN);
    exit_section_(b, m, null, r);
    return r;
  }

  // mtype_subtype '='
  private static boolean mtype_1_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_1_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mtype_subtype(b, l + 1);
    r = r && consumeToken(b, OP_ASGN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' mtype_value)*
  private static boolean mtype_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!mtype_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mtype_4", c)) break;
    }
    return true;
  }

  // ',' mtype_value
  private static boolean mtype_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && mtype_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ':' identifier
  static boolean mtype_subtype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_subtype")) return false;
    if (!nextTokenIs(b, COL)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COL);
    p = r; // pin = 1
    r = r && identifier(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier
  public static boolean mtype_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtype_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MTYPE_VALUE, "<mtype value>");
    r = identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'mtype' mtype_subtype?
  public static boolean mtypename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtypename")) return false;
    if (!nextTokenIs(b, MTYPE_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MTYPE_KW);
    r = r && mtypename_1(b, l + 1);
    exit_section_(b, m, MTYPENAME, r);
    return r;
  }

  // mtype_subtype?
  private static boolean mtypename_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtypename_1")) return false;
    mtype_subtype(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // unary_const_expression <<consumeMultiplicativeOperation>> multiplication_const_expression | unary_const_expression
  public static boolean multiplication_const_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplication_const_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, MULTIPLICATION_CONST_EXPRESSION, "<multiplication const expression>");
    r = multiplication_const_expression_0(b, l + 1);
    if (!r) r = unary_const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // unary_const_expression <<consumeMultiplicativeOperation>> multiplication_const_expression
  private static boolean multiplication_const_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiplication_const_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = unary_const_expression(b, l + 1);
    r = r && consumeMultiplicativeOperation(b, l + 1);
    r = r && multiplication_const_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'never' identifier? sequence_block
  public static boolean never(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "never")) return false;
    if (!nextTokenIs(b, NEVER_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, NEVER, null);
    r = consumeToken(b, NEVER_KW);
    p = r; // pin = 1
    r = r && report_error_(b, never_1(b, l + 1));
    r = p && sequence_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // identifier?
  private static boolean never_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "never_1")) return false;
    identifier(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'notrace'
  public static boolean no_trace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "no_trace")) return false;
    if (!nextTokenIs(b, NOTRACE_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOTRACE_KW);
    exit_section_(b, m, NO_TRACE, r);
    return r;
  }

  /* ********************************************************** */
  // var ':' any_expression '..' any_expression
  static boolean number_range(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_range")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_$(b, l + 1);
    r = r && consumeToken(b, COL);
    r = r && any_expression(b, l + 1, -1);
    r = r && consumeToken(b, DOTDOT);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<consumeNumber>>
  public static boolean number_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "number_rule")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NUMBER_RULE, "<number rule>");
    r = consumeNumber(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // visible? (one_decl_with_typename | unsigned_decl)
  public static boolean one_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ONE_DECL, "<one decl>");
    r = one_decl_0(b, l + 1);
    r = r && one_decl_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // visible?
  private static boolean one_decl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_0")) return false;
    visible(b, l + 1);
    return true;
  }

  // one_decl_with_typename | unsigned_decl
  private static boolean one_decl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_1")) return false;
    boolean r;
    r = one_decl_with_typename(b, l + 1);
    if (!r) r = unsigned_decl(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // <<eolOrSemiRaw>> typename eol_or_semi*
  static boolean one_decl_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_type")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = eolOrSemiRaw(b, l + 1);
    r = r && typename(b, l + 1);
    p = r; // pin = 2
    r = r && one_decl_type_2(b, l + 1);
    register_hook_(b, POP_REMAPPER, null);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // eol_or_semi*
  private static boolean one_decl_type_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_type_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, EOL_OR_SEMI)) break;
      if (!empty_element_parsed_guard_(b, "one_decl_type_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // one_decl_type ivar (',' ivar) *
  static boolean one_decl_with_typename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_with_typename")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = one_decl_type(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, ivar(b, l + 1));
    r = p && one_decl_with_typename_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (',' ivar) *
  private static boolean one_decl_with_typename_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_with_typename_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!one_decl_with_typename_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "one_decl_with_typename_2", c)) break;
    }
    return true;
  }

  // ',' ivar
  private static boolean one_decl_with_typename_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "one_decl_with_typename_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ivar(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '::' sequence semicolon_or_arrow?
  public static boolean option(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option")) return false;
    if (!nextTokenIs(b, SEP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OPTION, null);
    r = consumeToken(b, SEP);
    p = r; // pin = 1
    r = r && report_error_(b, sequence(b, l + 1));
    r = p && option_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semicolon_or_arrow?
  private static boolean option_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "option_2")) return false;
    semicolon_or_arrow(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // option+
  static boolean options(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "options")) return false;
    if (!nextTokenIs(b, SEP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = option(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!option(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "options", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' parenthesis_remapper <<param>> ')'
  public static boolean parenthesis(PsiBuilder b, int l, Parser _param) {
    if (!recursion_guard_(b, l, "parenthesis")) return false;
    if (!nextTokenIs(b, OPENBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPENBRACKET);
    r = r && parenthesis_remapper(b, l + 1);
    r = r && _param.parse(b, l);
    r = r && consumeToken(b, CLOSEBRACKET);
    register_hook_(b, POP_REMAPPER, null);
    exit_section_(b, m, PARENTHESIS, r);
    return r;
  }

  /* ********************************************************** */
  // <<eolOrSemiAsEOL>>
  static boolean parenthesis_remapper(PsiBuilder b, int l) {
    return eolOrSemiAsEOL(b, l + 1);
  }

  /* ********************************************************** */
  // '[' any_expression ']'
  static boolean pid_ref_opt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_ref_opt")) return false;
    if (!nextTokenIs(b, OPENSQUAREBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPENSQUAREBRACKET);
    r = r && any_expression(b, l + 1, -1);
    r = r && consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // varref '?' '[' recv_args ']'	/* poll without side-effect */
  // 	| varref '?' '?' '[' recv_args ']'
  public static boolean poll(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poll")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POLL, "<poll>");
    r = poll_0(b, l + 1);
    if (!r) r = poll_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // varref '?' '[' recv_args ']'
  private static boolean poll_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poll_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeTokens(b, 0, QMARK, OPENSQUAREBRACKET);
    r = r && recv_args(b, l + 1);
    r = r && consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  // varref '?' '?' '[' recv_args ']'
  private static boolean poll_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poll_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeTokens(b, 0, QMARK, QMARK, OPENSQUAREBRACKET);
    r = r && recv_args(b, l + 1);
    r = r && consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'printf' <<parenthesis (string [ ',' arg_lst ]) >>
  static boolean printf(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "printf")) return false;
    if (!nextTokenIs(b, PRINTF_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, PRINTF_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::printf_1_0);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // string [ ',' arg_lst ]
  private static boolean printf_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "printf_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRING);
    r = r && printf_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ',' arg_lst ]
  private static boolean printf_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "printf_1_0_1")) return false;
    printf_1_0_1_0(b, l + 1);
    return true;
  }

  // ',' arg_lst
  private static boolean printf_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "printf_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && arg_lst(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'printm' <<parenthesis (varref|const) >>
  static boolean printm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "printm")) return false;
    if (!nextTokenIs(b, PRINTM_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, PRINTM_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::printm_1_0);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // varref|const
  private static boolean printm_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "printm_1_0")) return false;
    boolean r;
    r = varref(b, l + 1);
    if (!r) r = const_$(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'priority' const
  public static boolean priority(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "priority")) return false;
    if (!nextTokenIs(b, PRIORITY_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PRIORITY, null);
    r = consumeToken(b, PRIORITY_KW);
    p = r; // pin = 1
    r = r && const_$(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // proctype_declaration sequence_block
  public static boolean proctype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROCTYPE, "<proctype>");
    r = proctype_declaration(b, l + 1);
    p = r; // pin = 1
    r = r && sequence_block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // !(')') decl_lst
  static boolean proctype_decl_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_decl_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = proctype_decl_list_0(b, l + 1);
    r = r && decl_lst(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::proctype_decl_list_r);
    return r;
  }

  // !(')')
  private static boolean proctype_decl_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_decl_list_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, CLOSEBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // !(')')
  static boolean proctype_decl_list_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_decl_list_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, CLOSEBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // proctype_header  identifier <<parenthesis [ proctype_decl_list ]>> [ priority ] [ enabler ]
  static boolean proctype_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_declaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = proctype_header(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, identifier(b, l + 1));
    r = p && report_error_(b, parenthesis(b, l + 1, PromelaParser::proctype_declaration_2_0)) && r;
    r = p && report_error_(b, proctype_declaration_3(b, l + 1)) && r;
    r = p && proctype_declaration_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, PromelaParser::proctype_declaration_r);
    return r || p;
  }

  // [ proctype_decl_list ]
  private static boolean proctype_declaration_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_declaration_2_0")) return false;
    proctype_decl_list(b, l + 1);
    return true;
  }

  // [ priority ]
  private static boolean proctype_declaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_declaration_3")) return false;
    priority(b, l + 1);
    return true;
  }

  // [ enabler ]
  private static boolean proctype_declaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_declaration_4")) return false;
    enabler(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !'{'
  static boolean proctype_declaration_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_declaration_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, OPENCURLYBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // proctype_type | active_proctype
  public static boolean proctype_header(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_header")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROCTYPE_HEADER, "<proctype header>");
    r = proctype_type(b, l + 1);
    if (!r) r = active_proctype(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'proctype' | d_proctype
  static boolean proctype_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proctype_type")) return false;
    if (!nextTokenIs(b, "", PROCTYPE_D_KW, PROCTYPE_KW)) return false;
    boolean r;
    r = consumeToken(b, PROCTYPE_KW);
    if (!r) r = d_proctype(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // identifier [ indexer ]
  public static boolean property_access(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_access")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY_ACCESS, "<property access>");
    r = identifier(b, l + 1);
    r = r && property_access_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ indexer ]
  private static boolean property_access_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_access_1")) return false;
    indexer(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // number_range
  // 	| var 'in' varref
  public static boolean range(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RANGE, "<range>");
    r = number_range(b, l + 1);
    if (!r) r = range_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // var 'in' varref
  private static boolean range_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_$(b, l + 1);
    r = r && consumeToken(b, IN_KW);
    r = r && varref(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // varref '?' recv_args		/* normal receive */
  // 	| varref '?' '?' recv_args	/* random receive */
  // 	| varref '?' '<' recv_args '>'	/* poll with side-effect */
  // 	| varref '?' '?' '<' recv_args '>'
  public static boolean receive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RECEIVE, "<receive>");
    r = receive_0(b, l + 1);
    if (!r) r = receive_1(b, l + 1);
    if (!r) r = receive_2(b, l + 1);
    if (!r) r = receive_3(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // varref '?' recv_args
  private static boolean receive_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receive_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeToken(b, QMARK);
    r = r && recv_args(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // varref '?' '?' recv_args
  private static boolean receive_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receive_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeTokens(b, 0, QMARK, QMARK);
    r = r && recv_args(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // varref '?' '<' recv_args '>'
  private static boolean receive_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receive_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeTokens(b, 0, QMARK, OP_LT);
    r = r && recv_args(b, l + 1);
    r = r && consumeToken(b, OP_GT);
    exit_section_(b, m, null, r);
    return r;
  }

  // varref '?' '?' '<' recv_args '>'
  private static boolean receive_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receive_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeTokens(b, 0, QMARK, QMARK, OP_LT);
    r = r && recv_args(b, l + 1);
    r = r && consumeToken(b, OP_GT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // varref | 'eval' <<parenthesis varref>> | [ '-' ] const
  public static boolean recv_arg(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_arg")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RECV_ARG, "<recv arg>");
    r = varref(b, l + 1);
    if (!r) r = recv_arg_1(b, l + 1);
    if (!r) r = recv_arg_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'eval' <<parenthesis varref>>
  private static boolean recv_arg_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_arg_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EVAL_KW);
    r = r && parenthesis(b, l + 1, PromelaParser::varref);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '-' ] const
  private static boolean recv_arg_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_arg_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = recv_arg_2_0(b, l + 1);
    r = r && const_$(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '-' ]
  private static boolean recv_arg_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_arg_2_0")) return false;
    consumeToken(b, OP_MINUS);
    return true;
  }

  /* ********************************************************** */
  // recv_arg <<parenthesis recv_args>> | recv_arg ( ',' recv_arg ) *
  public static boolean recv_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_args")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RECV_ARGS, "<recv args>");
    r = recv_args_0(b, l + 1);
    if (!r) r = recv_args_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // recv_arg <<parenthesis recv_args>>
  private static boolean recv_args_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_args_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = recv_arg(b, l + 1);
    r = r && parenthesis(b, l + 1, PromelaParser::recv_args);
    exit_section_(b, m, null, r);
    return r;
  }

  // recv_arg ( ',' recv_arg ) *
  private static boolean recv_args_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_args_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = recv_arg(b, l + 1);
    r = r && recv_args_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' recv_arg ) *
  private static boolean recv_args_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_args_1_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!recv_args_1_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "recv_args_1_1", c)) break;
    }
    return true;
  }

  // ',' recv_arg
  private static boolean recv_args_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recv_args_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && recv_arg(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<eolOrSemiAsEOL>> modules
  static boolean root(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "root")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = eolOrSemiAsEOL(b, l + 1);
    r = r && modules(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'run' identifier <<parenthesis [ arg_lst ]>> [ priority ]
  public static boolean run(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "run")) return false;
    if (!nextTokenIs(b, RUN_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RUN, null);
    r = consumeToken(b, RUN_KW);
    p = r; // pin = 1
    r = r && report_error_(b, identifier(b, l + 1));
    r = p && report_error_(b, parenthesis(b, l + 1, PromelaParser::run_2_0)) && r;
    r = p && run_3(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ arg_lst ]
  private static boolean run_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "run_2_0")) return false;
    arg_lst(b, l + 1);
    return true;
  }

  // [ priority ]
  private static boolean run_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "run_3")) return false;
    priority(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'select' <<parenthesis number_range >>
  static boolean select(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "select")) return false;
    if (!nextTokenIs(b, SELECT_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, SELECT_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::number_range);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // ((semi | arrow) arrow) | semicolon_rule | arrow
  static boolean semicolon_or_arrow(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semicolon_or_arrow")) return false;
    if (!nextTokenIs(b, "", ARROW, SEMI)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_or_arrow_0(b, l + 1);
    if (!r) r = semicolon_rule(b, l + 1);
    if (!r) r = consumeToken(b, ARROW);
    exit_section_(b, m, null, r);
    return r;
  }

  // (semi | arrow) arrow
  private static boolean semicolon_or_arrow_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semicolon_or_arrow_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_or_arrow_0_0(b, l + 1);
    r = r && consumeToken(b, ARROW);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi | arrow
  private static boolean semicolon_or_arrow_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semicolon_or_arrow_0_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, ARROW);
    return r;
  }

  /* ********************************************************** */
  // ((semi | arrow) semi) | semi
  static boolean semicolon_rule(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semicolon_rule")) return false;
    if (!nextTokenIs(b, "", ARROW, SEMI)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_rule_0(b, l + 1);
    if (!r) r = consumeToken(b, SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  // (semi | arrow) semi
  private static boolean semicolon_rule_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semicolon_rule_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_rule_0_0(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  // semi | arrow
  private static boolean semicolon_rule_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "semicolon_rule_0_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, ARROW);
    return r;
  }

  /* ********************************************************** */
  // varref '!' send_args		/* normal fifo send */
  // 	| varref '!' '!' send_args
  public static boolean send(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "send")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEND, "<send>");
    r = send_0(b, l + 1);
    if (!r) r = send_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // varref '!' send_args
  private static boolean send_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "send_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeToken(b, OP_BANG);
    r = r && send_args(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // varref '!' '!' send_args
  private static boolean send_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "send_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = varref(b, l + 1);
    r = r && consumeTokens(b, 0, OP_BANG, OP_BANG);
    r = r && send_args(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // any_expression <<parenthesis arg_lst >> | arg_lst
  public static boolean send_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "send_args")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEND_ARGS, "<send args>");
    r = send_args_0(b, l + 1);
    if (!r) r = arg_lst(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // any_expression <<parenthesis arg_lst >>
  private static boolean send_args_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "send_args_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = any_expression(b, l + 1, -1);
    r = r && parenthesis(b, l + 1, PromelaParser::arg_lst);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<eolOrSemiAsSemi>> step (semicolon_or_arrow+ step)*
  public static boolean sequence(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SEQUENCE, "<sequence>");
    r = eolOrSemiAsSemi(b, l + 1);
    r = r && step(b, l + 1);
    r = r && sequence_2(b, l + 1);
    register_hook_(b, POP_REMAPPER, null);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (semicolon_or_arrow+ step)*
  private static boolean sequence_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!sequence_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sequence_2", c)) break;
    }
    return true;
  }

  // semicolon_or_arrow+ step
  private static boolean sequence_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sequence_2_0_0(b, l + 1);
    r = r && step(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // semicolon_or_arrow+
  private static boolean sequence_2_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_2_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_or_arrow(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!semicolon_or_arrow(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sequence_2_0_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' sequence semicolon_or_arrow? close_curly_bracket
  public static boolean sequence_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_block")) return false;
    if (!nextTokenIs(b, OPENCURLYBRACKET)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SEQUENCE_BLOCK, null);
    r = consumeToken(b, OPENCURLYBRACKET);
    p = r; // pin = 1
    r = r && report_error_(b, sequence(b, l + 1));
    r = p && report_error_(b, sequence_block_2(b, l + 1)) && r;
    r = p && close_curly_bracket(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // semicolon_or_arrow?
  private static boolean sequence_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sequence_block_2")) return false;
    semicolon_or_arrow(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '[' size_initializer_const_expression ']'
  public static boolean size_initializer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "size_initializer")) return false;
    if (!nextTokenIs(b, OPENSQUAREBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OPENSQUAREBRACKET);
    r = r && size_initializer_const_expression(b, l + 1);
    r = r && consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, m, SIZE_INITIALIZER, r);
    return r;
  }

  /* ********************************************************** */
  // const_expression
  static boolean size_initializer_const_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "size_initializer_const_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::size_initializer_const_expression_r);
    return r;
  }

  /* ********************************************************** */
  // !']'
  static boolean size_initializer_const_expression_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "size_initializer_const_expression_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, CLOSESQUAREBRACKET);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // decl_lst
  //     | stmnt ('unless' stmnt)?
  //     | xrxs
  public static boolean step(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STEP, "<step>");
    r = decl_lst(b, l + 1);
    if (!r) r = step_1(b, l + 1);
    if (!r) r = xrxs(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::step_recover);
    return r;
  }

  // stmnt ('unless' stmnt)?
  private static boolean step_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmnt(b, l + 1);
    r = r && step_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('unless' stmnt)?
  private static boolean step_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_1_1")) return false;
    step_1_1_0(b, l + 1);
    return true;
  }

  // 'unless' stmnt
  private static boolean step_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, UNLESS_KW);
    r = r && stmnt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !('od'|'fi'|'::' | '}' | semicolon_or_arrow)
  static boolean step_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !step_recover_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'od'|'fi'|'::' | '}' | semicolon_or_arrow
  private static boolean step_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "step_recover_0")) return false;
    boolean r;
    r = consumeToken(b, DO_END_KW);
    if (!r) r = consumeToken(b, IF_END_KW);
    if (!r) r = consumeToken(b, SEP);
    if (!r) r = consumeToken(b, CLOSECURLYBRACKET);
    if (!r) r = semicolon_or_arrow(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // if_stmnt
  // 	| atomic_stmnt
  // 	| for_stmnt
  //     | d_step
  //     | select
  // 	| do_stmnt
  // 	| 'else'			/* used inside options */
  // 	| 'break'			/* used inside iterations */
  // 	| 'skip'
  // 	| goto
  // 	| printf
  // 	| printm
  // 	| assert
  // 	| ccode
  // 	| sequence_block	/* normal sequence */
  //     | label stmnt	/* labeled statement */
  //     | assign
  // 	| send
  //     | receive
  //     | inline_call
  //     | 'return' base_expression // not in the official grammar
  //     | base_expression
  public static boolean stmnt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmnt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STMNT, "<stmnt>");
    r = if_stmnt(b, l + 1);
    if (!r) r = atomic_stmnt(b, l + 1);
    if (!r) r = for_stmnt(b, l + 1);
    if (!r) r = d_step(b, l + 1);
    if (!r) r = select(b, l + 1);
    if (!r) r = do_stmnt(b, l + 1);
    if (!r) r = consumeToken(b, ELSE_KW);
    if (!r) r = consumeToken(b, BREAK_KW);
    if (!r) r = consumeToken(b, CONST_SKIP);
    if (!r) r = goto_$(b, l + 1);
    if (!r) r = printf(b, l + 1);
    if (!r) r = printm(b, l + 1);
    if (!r) r = assert_$(b, l + 1);
    if (!r) r = ccode(b, l + 1);
    if (!r) r = sequence_block(b, l + 1);
    if (!r) r = stmnt_15(b, l + 1);
    if (!r) r = assign(b, l + 1);
    if (!r) r = send(b, l + 1);
    if (!r) r = receive(b, l + 1);
    if (!r) r = inline_call(b, l + 1);
    if (!r) r = stmnt_20(b, l + 1);
    if (!r) r = base_expression(b, l + 1, -1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // label stmnt
  private static boolean stmnt_15(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmnt_15")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label(b, l + 1);
    r = r && stmnt(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'return' base_expression
  private static boolean stmnt_20(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmnt_20")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RETURN_KW);
    r = r && base_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('trace' | no_trace) sequence_block
  public static boolean trace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trace")) return false;
    if (!nextTokenIs(b, "<trace>", NOTRACE_KW, TRACE_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TRACE, "<trace>");
    r = trace_0(b, l + 1);
    p = r; // pin = 1
    r = r && sequence_block(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'trace' | no_trace
  private static boolean trace_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "trace_0")) return false;
    boolean r;
    r = consumeToken(b, TRACE_KW);
    if (!r) r = no_trace(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // 'bit' | 'bool' | 'byte' | 'short' | 'int' | mtypename | 'chan'
  // 	| custom_typename
  public static boolean typename(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "typename")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPENAME, "<typename>");
    r = consumeToken(b, BIT_TYPE_KW);
    if (!r) r = consumeToken(b, BOOL_TYPE_KW);
    if (!r) r = consumeToken(b, BYTE_TYPE_KW);
    if (!r) r = consumeToken(b, SHORT_TYPE_KW);
    if (!r) r = consumeToken(b, INT_TYPE_KW);
    if (!r) r = mtypename(b, l + 1);
    if (!r) r = consumeToken(b, CHAN_TYPE_KW);
    if (!r) r = custom_typename(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // <<consumeUnOperation>>
  static boolean unarop(PsiBuilder b, int l) {
    return consumeUnOperation(b, l + 1);
  }

  /* ********************************************************** */
  // '-' unit_const_expression | unit_const_expression
  public static boolean unary_const_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_const_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, UNARY_CONST_EXPRESSION, "<unary const expression>");
    r = unary_const_expression_0(b, l + 1);
    if (!r) r = unit_const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // '-' unit_const_expression
  private static boolean unary_const_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_const_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_MINUS);
    r = r && unit_const_expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // const | wrap_const_expression
  public static boolean unit_const_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unit_const_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNIT_CONST_EXPRESSION, "<unit const expression>");
    r = const_$(b, l + 1);
    if (!r) r = wrap_const_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'unsigned' identifier ':' const [ '=' any_expression ]
  public static boolean unsigned_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unsigned_decl")) return false;
    if (!nextTokenIs(b, UNSIGNED_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UNSIGNED_DECL, null);
    r = consumeToken(b, UNSIGNED_KW);
    p = r; // pin = 1
    r = r && report_error_(b, identifier(b, l + 1));
    r = p && report_error_(b, consumeToken(b, COL)) && r;
    r = p && report_error_(b, const_$(b, l + 1)) && r;
    r = p && unsigned_decl_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ '=' any_expression ]
  private static boolean unsigned_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unsigned_decl_4")) return false;
    unsigned_decl_4_0(b, l + 1);
    return true;
  }

  // '=' any_expression
  private static boolean unsigned_decl_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unsigned_decl_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, OP_ASGN);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'typedef' <<captureUname>> identifier '{' <<eolOrSemiAsSemi>> utype_decl_list close_curly_bracket
  public static boolean utype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "utype")) return false;
    if (!nextTokenIs(b, TYPEDEF_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, UTYPE, null);
    r = consumeToken(b, TYPEDEF_KW);
    p = r; // pin = 1
    r = r && report_error_(b, captureUname(b, l + 1));
    r = p && report_error_(b, identifier(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, OPENCURLYBRACKET)) && r;
    r = p && report_error_(b, eolOrSemiAsSemi(b, l + 1)) && r;
    r = p && report_error_(b, utype_decl_list(b, l + 1)) && r;
    r = p && close_curly_bracket(b, l + 1) && r;
    register_hook_(b, USER_TYPE_ADDED, null);
    register_hook_(b, POP_REMAPPER, null);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // one_decl (semicolon_rule one_decl) *
  static boolean utype_decl_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "utype_decl_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_);
    r = one_decl(b, l + 1);
    r = r && utype_decl_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, PromelaParser::utype_one_decl_r);
    return r;
  }

  // (semicolon_rule one_decl) *
  private static boolean utype_decl_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "utype_decl_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!utype_decl_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "utype_decl_list_1", c)) break;
    }
    return true;
  }

  // semicolon_rule one_decl
  private static boolean utype_decl_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "utype_decl_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = semicolon_rule(b, l + 1);
    r = r && one_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(close_curly_bracket)
  static boolean utype_one_decl_r(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "utype_one_decl_r")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !utype_one_decl_r_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (close_curly_bracket)
  private static boolean utype_one_decl_r_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "utype_one_decl_r_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = close_curly_bracket(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // identifier
  public static boolean var_$(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_$")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VAR, "<var>");
    r = identifier(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // var [ indexer ] ('.' property_access)*
  public static boolean varref(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varref")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARREF, "<varref>");
    r = var_$(b, l + 1);
    r = r && varref_1(b, l + 1);
    r = r && varref_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ indexer ]
  private static boolean varref_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varref_1")) return false;
    indexer(b, l + 1);
    return true;
  }

  // ('.' property_access)*
  private static boolean varref_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varref_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!varref_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "varref_2", c)) break;
    }
    return true;
  }

  // '.' property_access
  private static boolean varref_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varref_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT);
    r = r && property_access(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'hidden' | 'show' | 'local'
  public static boolean visible(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "visible")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VISIBLE, "<visible>");
    r = consumeToken(b, HIDDEN_KW);
    if (!r) r = consumeToken(b, SHOW_KW);
    if (!r) r = consumeToken(b, LOCAL_KW);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // <<parenthesis const_expression>>
  static boolean wrap_const_expression(PsiBuilder b, int l) {
    return parenthesis(b, l + 1, PromelaParser::const_expression);
  }

  /* ********************************************************** */
  // ('xr' | 'xs') varref (',' varref ) *
  static boolean xrxs(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xrxs")) return false;
    if (!nextTokenIs(b, "", XR_KW, XS_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = xrxs_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, varref(b, l + 1));
    r = p && xrxs_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'xr' | 'xs'
  private static boolean xrxs_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xrxs_0")) return false;
    boolean r;
    r = consumeToken(b, XR_KW);
    if (!r) r = consumeToken(b, XS_KW);
    return r;
  }

  // (',' varref ) *
  private static boolean xrxs_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xrxs_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!xrxs_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "xrxs_2", c)) break;
    }
    return true;
  }

  // ',' varref
  private static boolean xrxs_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "xrxs_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && varref(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: any_expression
  // Operator priority table:
  // 0: ATOM(wrap_any_expression)
  // 1: BINARY(binary_any_expression)
  // 2: ATOM(pid_refer_any_expression)
  // 3: PREFIX(unary_any_expression)
  // 4: ATOM(ternary_any_expression)
  // 5: ATOM(len_any_expression)
  // 6: ATOM(poll_any_expression)
  // 7: ATOM(varref_any_expression)
  // 8: ATOM(cexpr_any_expression)
  // 9: ATOM(const_any_expression)
  // 10: ATOM(timeout_any_expression)
  // 11: ATOM(np_any_expression)
  // 12: ATOM(enabled_any_expression)
  // 13: ATOM(pc_value_any_expression)
  // 14: ATOM(run_any_expression)
  // 15: ATOM(get_priority_any_expression)
  // 16: ATOM(set_priority_any_expression)
  public static boolean any_expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "any_expression")) return false;
    addVariant(b, "<any expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<any expression>");
    r = wrap_any_expression(b, l + 1);
    if (!r) r = pid_refer_any_expression(b, l + 1);
    if (!r) r = unary_any_expression(b, l + 1);
    if (!r) r = ternary_any_expression(b, l + 1);
    if (!r) r = len_any_expression(b, l + 1);
    if (!r) r = poll_any_expression(b, l + 1);
    if (!r) r = varref_any_expression(b, l + 1);
    if (!r) r = cexpr_any_expression(b, l + 1);
    if (!r) r = const_any_expression(b, l + 1);
    if (!r) r = timeout_any_expression(b, l + 1);
    if (!r) r = np_any_expression(b, l + 1);
    if (!r) r = enabled_any_expression(b, l + 1);
    if (!r) r = pc_value_any_expression(b, l + 1);
    if (!r) r = run_any_expression(b, l + 1);
    if (!r) r = get_priority_any_expression(b, l + 1);
    if (!r) r = set_priority_any_expression(b, l + 1);
    p = r;
    r = r && any_expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean any_expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "any_expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 1 && binarop(b, l + 1)) {
        r = any_expression(b, l, 1);
        exit_section_(b, l, m, BINARY_ANY_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // <<parenthesis any_expression>>
  public static boolean wrap_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "wrap_any_expression")) return false;
    if (!nextTokenIsSmart(b, OPENBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, m, WRAP_ANY_EXPRESSION, r);
    return r;
  }

  // var pid_ref_opt? '@' var
  //     | var pid_ref_opt? ':' var
  public static boolean pid_refer_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_any_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PID_REFER_ANY_EXPRESSION, "<pid refer any expression>");
    r = pid_refer_any_expression_0(b, l + 1);
    if (!r) r = pid_refer_any_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // var pid_ref_opt? '@' var
  private static boolean pid_refer_any_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_any_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_$(b, l + 1);
    r = r && pid_refer_any_expression_0_1(b, l + 1);
    r = r && consumeToken(b, AT);
    r = r && var_$(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // pid_ref_opt?
  private static boolean pid_refer_any_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_any_expression_0_1")) return false;
    pid_ref_opt(b, l + 1);
    return true;
  }

  // var pid_ref_opt? ':' var
  private static boolean pid_refer_any_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_any_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_$(b, l + 1);
    r = r && pid_refer_any_expression_1_1(b, l + 1);
    r = r && consumeToken(b, COL);
    r = r && var_$(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // pid_ref_opt?
  private static boolean pid_refer_any_expression_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_any_expression_1_1")) return false;
    pid_ref_opt(b, l + 1);
    return true;
  }

  public static boolean unary_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_any_expression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = unarop(b, l + 1);
    p = r;
    r = p && any_expression(b, l, 3);
    exit_section_(b, l, m, UNARY_ANY_EXPRESSION, r, p, null);
    return r || p;
  }

  // <<parenthesis (any_expression '->' any_expression ':' any_expression)>>
  public static boolean ternary_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_any_expression")) return false;
    if (!nextTokenIsSmart(b, OPENBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parenthesis(b, l + 1, PromelaParser::ternary_any_expression_0_0);
    exit_section_(b, m, TERNARY_ANY_EXPRESSION, r);
    return r;
  }

  // any_expression '->' any_expression ':' any_expression
  private static boolean ternary_any_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_any_expression_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = any_expression(b, l + 1, -1);
    r = r && consumeToken(b, ARROW);
    r = r && any_expression(b, l + 1, -1);
    r = r && consumeToken(b, COL);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // len
  public static boolean len_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "len_any_expression")) return false;
    if (!nextTokenIsSmart(b, LEN_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = len(b, l + 1);
    exit_section_(b, m, LEN_ANY_EXPRESSION, r);
    return r;
  }

  // poll
  public static boolean poll_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poll_any_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POLL_ANY_EXPRESSION, "<poll any expression>");
    r = poll(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // varref
  public static boolean varref_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varref_any_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARREF_ANY_EXPRESSION, "<varref any expression>");
    r = varref(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // c_expr_kw ccode_body
  public static boolean cexpr_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cexpr_any_expression")) return false;
    if (!nextTokenIsSmart(b, C_EXPR_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, C_EXPR_KW);
    r = r && ccode_body(b, l + 1);
    exit_section_(b, m, CEXPR_ANY_EXPRESSION, r);
    return r;
  }

  // const
  public static boolean const_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_any_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_ANY_EXPRESSION, "<const any expression>");
    r = const_$(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'timeout'
  public static boolean timeout_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "timeout_any_expression")) return false;
    if (!nextTokenIsSmart(b, TIMEOUT_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, TIMEOUT_KW);
    exit_section_(b, m, TIMEOUT_ANY_EXPRESSION, r);
    return r;
  }

  // 'np_'
  public static boolean np_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "np_any_expression")) return false;
    if (!nextTokenIsSmart(b, NP_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, NP_KW);
    exit_section_(b, m, NP_ANY_EXPRESSION, r);
    return r;
  }

  // 'enabled' <<parenthesis any_expression>>
  public static boolean enabled_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enabled_any_expression")) return false;
    if (!nextTokenIsSmart(b, ENABLED_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENABLED_ANY_EXPRESSION, null);
    r = consumeTokenSmart(b, ENABLED_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'pc_value' <<parenthesis any_expression>>
  public static boolean pc_value_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pc_value_any_expression")) return false;
    if (!nextTokenIsSmart(b, PC_VALUE_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PC_VALUE_ANY_EXPRESSION, null);
    r = consumeTokenSmart(b, PC_VALUE_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // run
  public static boolean run_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "run_any_expression")) return false;
    if (!nextTokenIsSmart(b, RUN_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = run(b, l + 1);
    exit_section_(b, m, RUN_ANY_EXPRESSION, r);
    return r;
  }

  // 'get_priority' <<parenthesis any_expression>>
  public static boolean get_priority_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "get_priority_any_expression")) return false;
    if (!nextTokenIsSmart(b, GET_PRIORITY_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GET_PRIORITY_ANY_EXPRESSION, null);
    r = consumeTokenSmart(b, GET_PRIORITY_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'set_priority' <<parenthesis (any_expression ',' any_expression)>>
  public static boolean set_priority_any_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_priority_any_expression")) return false;
    if (!nextTokenIsSmart(b, SET_PRIORITY_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SET_PRIORITY_ANY_EXPRESSION, null);
    r = consumeTokenSmart(b, SET_PRIORITY_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::set_priority_any_expression_1_0);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // any_expression ',' any_expression
  private static boolean set_priority_any_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_priority_any_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = any_expression(b, l + 1, -1);
    r = r && consumeToken(b, COMMA);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: base_expression
  // Operator priority table:
  // 0: ATOM(wrap_base_expression)
  // 1: ATOM(chanpoll_base_expression)
  // 2: BINARY(binary_base_expression)
  // 3: BINARY(ltl_bin_base_expression)
  // 4: PREFIX(ltl_un_base_expression)
  // 5: ATOM(pid_refer_base_expression)
  // 6: PREFIX(unary_base_expression)
  // 7: ATOM(ternary_base_expression)
  // 8: ATOM(len_base_expression)
  // 9: ATOM(poll_base_expression)
  // 10: ATOM(varref_base_expression)
  // 11: ATOM(const_base_expression)
  // 12: ATOM(timeout_base_expression)
  // 13: ATOM(np_base_expression)
  // 14: ATOM(enabled_base_expression)
  // 15: ATOM(pc_value_base_expression)
  // 16: ATOM(run_base_expression)
  // 17: ATOM(get_priority_base_expression)
  // 18: ATOM(set_priority_base_expression)
  public static boolean base_expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "base_expression")) return false;
    addVariant(b, "<base expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<base expression>");
    r = wrap_base_expression(b, l + 1);
    if (!r) r = chanpoll_base_expression(b, l + 1);
    if (!r) r = ltl_un_base_expression(b, l + 1);
    if (!r) r = pid_refer_base_expression(b, l + 1);
    if (!r) r = unary_base_expression(b, l + 1);
    if (!r) r = ternary_base_expression(b, l + 1);
    if (!r) r = len_base_expression(b, l + 1);
    if (!r) r = poll_base_expression(b, l + 1);
    if (!r) r = varref_base_expression(b, l + 1);
    if (!r) r = const_base_expression(b, l + 1);
    if (!r) r = timeout_base_expression(b, l + 1);
    if (!r) r = np_base_expression(b, l + 1);
    if (!r) r = enabled_base_expression(b, l + 1);
    if (!r) r = pc_value_base_expression(b, l + 1);
    if (!r) r = run_base_expression(b, l + 1);
    if (!r) r = get_priority_base_expression(b, l + 1);
    if (!r) r = set_priority_base_expression(b, l + 1);
    p = r;
    r = r && base_expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean base_expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "base_expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 2 && binarop(b, l + 1)) {
        r = base_expression(b, l, 2);
        exit_section_(b, l, m, BINARY_BASE_EXPRESSION, r, true, null);
      }
      else if (g < 3 && ltl_bin_base_expression_0(b, l + 1)) {
        r = base_expression(b, l, 3);
        exit_section_(b, l, m, LTL_BIN_BASE_EXPRESSION, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // <<parenthesis base_expression>>
  public static boolean wrap_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "wrap_base_expression")) return false;
    if (!nextTokenIsSmart(b, OPENBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parenthesis(b, l + 1, base_expression_parser_);
    exit_section_(b, m, WRAP_BASE_EXPRESSION, r);
    return r;
  }

  // chanpoll <<parenthesis varref>>
  public static boolean chanpoll_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "chanpoll_base_expression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CHANPOLL_BASE_EXPRESSION, "<chanpoll base expression>");
    r = chanpoll(b, l + 1);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::varref);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // <<ltlEnabled>> ltl_binop
  private static boolean ltl_bin_base_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_bin_base_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ltlEnabled(b, l + 1);
    r = r && ltl_binop(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean ltl_un_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_un_base_expression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = ltl_un_base_expression_0(b, l + 1);
    p = r;
    r = p && base_expression(b, l, 4);
    exit_section_(b, l, m, LTL_UN_BASE_EXPRESSION, r, p, null);
    return r || p;
  }

  // <<ltlEnabled>> ltl_unop
  private static boolean ltl_un_base_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ltl_un_base_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ltlEnabled(b, l + 1);
    r = r && ltl_unop(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // var pid_ref_opt? '@' var
  //     | var pid_ref_opt? ':' var
  public static boolean pid_refer_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_base_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PID_REFER_BASE_EXPRESSION, "<pid refer base expression>");
    r = pid_refer_base_expression_0(b, l + 1);
    if (!r) r = pid_refer_base_expression_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // var pid_ref_opt? '@' var
  private static boolean pid_refer_base_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_base_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_$(b, l + 1);
    r = r && pid_refer_base_expression_0_1(b, l + 1);
    r = r && consumeToken(b, AT);
    r = r && var_$(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // pid_ref_opt?
  private static boolean pid_refer_base_expression_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_base_expression_0_1")) return false;
    pid_ref_opt(b, l + 1);
    return true;
  }

  // var pid_ref_opt? ':' var
  private static boolean pid_refer_base_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_base_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_$(b, l + 1);
    r = r && pid_refer_base_expression_1_1(b, l + 1);
    r = r && consumeToken(b, COL);
    r = r && var_$(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // pid_ref_opt?
  private static boolean pid_refer_base_expression_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pid_refer_base_expression_1_1")) return false;
    pid_ref_opt(b, l + 1);
    return true;
  }

  public static boolean unary_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_base_expression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = unarop(b, l + 1);
    p = r;
    r = p && base_expression(b, l, 6);
    exit_section_(b, l, m, UNARY_BASE_EXPRESSION, r, p, null);
    return r || p;
  }

  // <<parenthesis (any_expression '->' any_expression ':' any_expression)>>
  public static boolean ternary_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_base_expression")) return false;
    if (!nextTokenIsSmart(b, OPENBRACKET)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parenthesis(b, l + 1, PromelaParser::ternary_base_expression_0_0);
    exit_section_(b, m, TERNARY_BASE_EXPRESSION, r);
    return r;
  }

  // any_expression '->' any_expression ':' any_expression
  private static boolean ternary_base_expression_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ternary_base_expression_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = any_expression(b, l + 1, -1);
    r = r && consumeToken(b, ARROW);
    r = r && any_expression(b, l + 1, -1);
    r = r && consumeToken(b, COL);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // len
  public static boolean len_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "len_base_expression")) return false;
    if (!nextTokenIsSmart(b, LEN_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = len(b, l + 1);
    exit_section_(b, m, LEN_BASE_EXPRESSION, r);
    return r;
  }

  // poll
  public static boolean poll_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "poll_base_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POLL_BASE_EXPRESSION, "<poll base expression>");
    r = poll(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // varref
  public static boolean varref_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "varref_base_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, VARREF_BASE_EXPRESSION, "<varref base expression>");
    r = varref(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // const
  public static boolean const_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_base_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_BASE_EXPRESSION, "<const base expression>");
    r = const_$(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'timeout'
  public static boolean timeout_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "timeout_base_expression")) return false;
    if (!nextTokenIsSmart(b, TIMEOUT_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, TIMEOUT_KW);
    exit_section_(b, m, TIMEOUT_BASE_EXPRESSION, r);
    return r;
  }

  // 'np_'
  public static boolean np_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "np_base_expression")) return false;
    if (!nextTokenIsSmart(b, NP_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, NP_KW);
    exit_section_(b, m, NP_BASE_EXPRESSION, r);
    return r;
  }

  // 'enabled' <<parenthesis any_expression>>
  public static boolean enabled_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "enabled_base_expression")) return false;
    if (!nextTokenIsSmart(b, ENABLED_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENABLED_BASE_EXPRESSION, null);
    r = consumeTokenSmart(b, ENABLED_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'pc_value' <<parenthesis any_expression>>
  public static boolean pc_value_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pc_value_base_expression")) return false;
    if (!nextTokenIsSmart(b, PC_VALUE_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PC_VALUE_BASE_EXPRESSION, null);
    r = consumeTokenSmart(b, PC_VALUE_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // run
  public static boolean run_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "run_base_expression")) return false;
    if (!nextTokenIsSmart(b, RUN_KW)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = run(b, l + 1);
    exit_section_(b, m, RUN_BASE_EXPRESSION, r);
    return r;
  }

  // 'get_priority' <<parenthesis any_expression>>
  public static boolean get_priority_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "get_priority_base_expression")) return false;
    if (!nextTokenIsSmart(b, GET_PRIORITY_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, GET_PRIORITY_BASE_EXPRESSION, null);
    r = consumeTokenSmart(b, GET_PRIORITY_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, any_expression_parser_);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // 'set_priority' <<parenthesis (any_expression ',' any_expression)>>
  public static boolean set_priority_base_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_priority_base_expression")) return false;
    if (!nextTokenIsSmart(b, SET_PRIORITY_KW)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SET_PRIORITY_BASE_EXPRESSION, null);
    r = consumeTokenSmart(b, SET_PRIORITY_KW);
    p = r; // pin = 1
    r = r && parenthesis(b, l + 1, PromelaParser::set_priority_base_expression_1_0);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // any_expression ',' any_expression
  private static boolean set_priority_base_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_priority_base_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = any_expression(b, l + 1, -1);
    r = r && consumeToken(b, COMMA);
    r = r && any_expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  static final Parser any_expression_parser_ = (b, l) -> any_expression(b, l + 1, -1);
  static final Parser base_expression_parser_ = (b, l) -> base_expression(b, l + 1, -1);
}
