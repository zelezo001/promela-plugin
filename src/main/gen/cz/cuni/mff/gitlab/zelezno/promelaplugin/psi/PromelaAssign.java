// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PromelaAssign extends PsiElement {

  @Nullable
  PromelaBaseExpression getBaseExpression();

  @Nullable
  PromelaInlineCall getInlineCall();

  @Nullable
  PromelaVarref getVarref();

  @Nullable
  PsiElement getIncdec();

}
