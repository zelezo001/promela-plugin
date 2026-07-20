// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.PromelaPsiImplUtil;

public class PromelaAssignImpl extends ASTWrapperPsiElement implements PromelaAssign {

  public PromelaAssignImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitAssign(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PromelaBaseExpression getBaseExpression() {
    return findChildByClass(PromelaBaseExpression.class);
  }

  @Override
  @Nullable
  public PromelaInlineCall getInlineCall() {
    return findChildByClass(PromelaInlineCall.class);
  }

  @Override
  @Nullable
  public PromelaVarref getVarref() {
    return findChildByClass(PromelaVarref.class);
  }

  @Override
  @Nullable
  public PsiElement getIncdec() {
    return findChildByType(INCDEC);
  }

}
