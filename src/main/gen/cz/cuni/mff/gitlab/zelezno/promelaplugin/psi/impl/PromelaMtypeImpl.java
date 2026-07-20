// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.MTypeImplMixin;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.PromelaPsiImplUtil;

public class PromelaMtypeImpl extends MTypeImplMixin implements PromelaMtype {

  public PromelaMtypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitMtype(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PromelaIdentifier getIdentifier() {
    return findChildByClass(PromelaIdentifier.class);
  }

  @Override
  @NotNull
  public List<PromelaMtypeValue> getMtypeValueList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaMtypeValue.class);
  }

}
