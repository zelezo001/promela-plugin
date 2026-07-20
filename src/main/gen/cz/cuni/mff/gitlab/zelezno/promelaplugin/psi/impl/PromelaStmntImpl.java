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

public class PromelaStmntImpl extends ASTWrapperPsiElement implements PromelaStmnt {

  public PromelaStmntImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PromelaVisitor visitor) {
    visitor.visitStmnt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PromelaVisitor) accept((PromelaVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PromelaAssign getAssign() {
    return findChildByClass(PromelaAssign.class);
  }

  @Override
  @Nullable
  public PromelaAtomicStmnt getAtomicStmnt() {
    return findChildByClass(PromelaAtomicStmnt.class);
  }

  @Override
  @Nullable
  public PromelaBaseExpression getBaseExpression() {
    return findChildByClass(PromelaBaseExpression.class);
  }

  @Override
  @Nullable
  public PromelaDoStmnt getDoStmnt() {
    return findChildByClass(PromelaDoStmnt.class);
  }

  @Override
  @Nullable
  public PromelaForStmnt getForStmnt() {
    return findChildByClass(PromelaForStmnt.class);
  }

  @Override
  @Nullable
  public PromelaGoto getGoto() {
    return findChildByClass(PromelaGoto.class);
  }

  @Override
  @Nullable
  public PromelaIfStmnt getIfStmnt() {
    return findChildByClass(PromelaIfStmnt.class);
  }

  @Override
  @Nullable
  public PromelaInlineCall getInlineCall() {
    return findChildByClass(PromelaInlineCall.class);
  }

  @Override
  @Nullable
  public PromelaLabel getLabel() {
    return findChildByClass(PromelaLabel.class);
  }

  @Override
  @Nullable
  public PromelaParenthesis getParenthesis() {
    return findChildByClass(PromelaParenthesis.class);
  }

  @Override
  @Nullable
  public PromelaReceive getReceive() {
    return findChildByClass(PromelaReceive.class);
  }

  @Override
  @Nullable
  public PromelaSend getSend() {
    return findChildByClass(PromelaSend.class);
  }

  @Override
  @Nullable
  public PromelaSequenceBlock getSequenceBlock() {
    return findChildByClass(PromelaSequenceBlock.class);
  }

  @Override
  @Nullable
  public PromelaStmnt getStmnt() {
    return findChildByClass(PromelaStmnt.class);
  }

}
