// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.IVarImplMixin;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.PromelaPsiImplUtil;

public class PromelaIvarImpl extends IVarImplMixin implements PromelaIvar {

  public PromelaIvarImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitIvar(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PromelaAnyExpression getAnyExpression() {
    return findChildByClass(PromelaAnyExpression.class);
  }

  @Override
  @Nullable
  public PromelaArrayInit getArrayInit() {
    return findChildByClass(PromelaArrayInit.class);
  }

  @Override
  @Nullable
  public PromelaChInit getChInit() {
    return findChildByClass(PromelaChInit.class);
  }

  @Override
  @NotNull
  public PromelaIdentifier getIdentifier() {
    return findNotNullChildByClass(PromelaIdentifier.class);
  }

  @Override
  @Nullable
  public PromelaSizeInitializer getSizeInitializer() {
    return findChildByClass(PromelaSizeInitializer.class);
  }

}
