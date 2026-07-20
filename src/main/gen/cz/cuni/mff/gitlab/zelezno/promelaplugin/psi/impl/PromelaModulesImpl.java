// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.ModulesImplMixin;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.PromelaPsiImplUtil;

public class PromelaModulesImpl extends ModulesImplMixin implements PromelaModules {

  public PromelaModulesImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitModules(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PromelaInit> getInitList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaInit.class);
  }

  @Override
  @NotNull
  public List<PromelaInline> getInlineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaInline.class);
  }

  @Override
  @NotNull
  public List<PromelaLtl> getLtlList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaLtl.class);
  }

  @Override
  @NotNull
  public List<PromelaMtype> getMtypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaMtype.class);
  }

  @Override
  @NotNull
  public List<PromelaNever> getNeverList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaNever.class);
  }

  @Override
  @NotNull
  public List<PromelaOneDecl> getOneDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaOneDecl.class);
  }

  @Override
  @NotNull
  public List<PromelaProctype> getProctypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaProctype.class);
  }

  @Override
  @NotNull
  public List<PromelaTrace> getTraceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaTrace.class);
  }

  @Override
  @NotNull
  public List<PromelaUtype> getUtypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PromelaUtype.class);
  }

}
