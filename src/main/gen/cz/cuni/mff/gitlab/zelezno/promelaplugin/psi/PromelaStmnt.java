// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PromelaStmnt extends PsiElement {

  @Nullable
  PromelaAssign getAssign();

  @Nullable
  PromelaAtomicStmnt getAtomicStmnt();

  @Nullable
  PromelaBaseExpression getBaseExpression();

  @Nullable
  PromelaDoStmnt getDoStmnt();

  @Nullable
  PromelaForStmnt getForStmnt();

  @Nullable
  PromelaGoto getGoto();

  @Nullable
  PromelaIfStmnt getIfStmnt();

  @Nullable
  PromelaInlineCall getInlineCall();

  @Nullable
  PromelaLabel getLabel();

  @Nullable
  PromelaParenthesis getParenthesis();

  @Nullable
  PromelaReceive getReceive();

  @Nullable
  PromelaSend getSend();

  @Nullable
  PromelaSequenceBlock getSequenceBlock();

  @Nullable
  PromelaStmnt getStmnt();

}
