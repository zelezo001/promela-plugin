// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.ProctypeImplMixin;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.*;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.impl.PromelaPsiImplUtil;

public class PromelaProctypeImpl extends ProctypeImplMixin implements PromelaProctype {

  public PromelaProctypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitProctype(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PromelaEnabler getEnabler() {
    return findChildByClass(PromelaEnabler.class);
  }

  @Override
  @Nullable
  public PromelaIdentifier getIdentifier() {
    return findChildByClass(PromelaIdentifier.class);
  }

  @Override
  @Nullable
  public PromelaParenthesis getParenthesis() {
    return findChildByClass(PromelaParenthesis.class);
  }

  @Override
  @Nullable
  public PromelaPriority getPriority() {
    return findChildByClass(PromelaPriority.class);
  }

  @Override
  @NotNull
  public PromelaProctypeHeader getProctypeHeader() {
    return findNotNullChildByClass(PromelaProctypeHeader.class);
  }

  @Override
  @Nullable
  public PromelaSequenceBlock getSequenceBlock() {
    return findChildByClass(PromelaSequenceBlock.class);
  }

}
