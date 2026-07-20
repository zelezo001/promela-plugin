// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.OneDeclImplMixin;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.PromelaPsiImplUtil;

public class PromelaOneDeclImpl extends OneDeclImplMixin implements PromelaOneDecl {

  public PromelaOneDeclImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitOneDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PromelaIvar> getIvarList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaIvar.class);
  }

  @Override
  @Nullable
  public PromelaTypename getTypename() {
    return findChildByClass(PromelaTypename.class);
  }

  @Override
  @Nullable
  public PromelaUnsignedDecl getUnsignedDecl() {
    return findChildByClass(PromelaUnsignedDecl.class);
  }

  @Override
  @Nullable
  public PromelaVisible getVisible() {
    return findChildByClass(PromelaVisible.class);
  }

}
