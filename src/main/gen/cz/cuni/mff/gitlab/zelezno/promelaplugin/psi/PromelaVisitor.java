// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.NamedPsiElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiTypeDeclaration;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.NavigablePsiElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariableReferencingElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiElementWithType;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaNamedElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.ConstantExpression;

public class PromelaVisitor extends PsiElementVisitor {

  public void visitActive(@NotNull PromelaActive o) {
    visitPsiElement(o);
  }

  public void visitActivePriority(@NotNull PromelaActivePriority o) {
    visitPsiElement(o);
  }

  public void visitAdditionConstExpression(@NotNull PromelaAdditionConstExpression o) {
    visitConstExpression(o);
  }

  public void visitAnyExpression(@NotNull PromelaAnyExpression o) {
    visitPsiElement(o);
  }

  public void visitArgLst(@NotNull PromelaArgLst o) {
    visitPsiElement(o);
  }

  public void visitArrayInit(@NotNull PromelaArrayInit o) {
    visitPsiElement(o);
  }

  public void visitAssign(@NotNull PromelaAssign o) {
    visitPsiElement(o);
  }

  public void visitAtomicStmnt(@NotNull PromelaAtomicStmnt o) {
    visitPsiElement(o);
  }

  public void visitBaseExpression(@NotNull PromelaBaseExpression o) {
    visitPsiElement(o);
  }

  public void visitBinaryAnyExpression(@NotNull PromelaBinaryAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitBinaryBaseExpression(@NotNull PromelaBinaryBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitCexprAnyExpression(@NotNull PromelaCexprAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitChInit(@NotNull PromelaChInit o) {
    visitPsiElement(o);
  }

  public void visitChInitTypes(@NotNull PromelaChInitTypes o) {
    visitPsiElement(o);
  }

  public void visitChanpoll(@NotNull PromelaChanpoll o) {
    visitPsiElement(o);
  }

  public void visitChanpollBaseExpression(@NotNull PromelaChanpollBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitConst(@NotNull PromelaConst o) {
    visitPsiElement(o);
  }

  public void visitConstAnyExpression(@NotNull PromelaConstAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitConstBaseExpression(@NotNull PromelaConstBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitConstExpression(@NotNull PromelaConstExpression o) {
    visitConstantExpression(o);
  }

  public void visitCustomTypename(@NotNull PromelaCustomTypename o) {
    visitNamedPsiElement(o);
    // visitPsiElementWithType(o);
  }

  public void visitDProctype(@NotNull PromelaDProctype o) {
    visitPsiElement(o);
  }

  public void visitDoStmnt(@NotNull PromelaDoStmnt o) {
    visitPsiElement(o);
  }

  public void visitEnabledAnyExpression(@NotNull PromelaEnabledAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitEnabledBaseExpression(@NotNull PromelaEnabledBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitEnabler(@NotNull PromelaEnabler o) {
    visitPsiElement(o);
  }

  public void visitForStmnt(@NotNull PromelaForStmnt o) {
    visitPsiElement(o);
  }

  public void visitGetPriorityAnyExpression(@NotNull PromelaGetPriorityAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitGetPriorityBaseExpression(@NotNull PromelaGetPriorityBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitGoto(@NotNull PromelaGoto o) {
    visitPsiElement(o);
  }

  public void visitIdentifier(@NotNull PromelaIdentifier o) {
    visitPsiElement(o);
  }

  public void visitIfStmnt(@NotNull PromelaIfStmnt o) {
    visitPsiElement(o);
  }

  public void visitIndexer(@NotNull PromelaIndexer o) {
    visitPsiElement(o);
  }

  public void visitInit(@NotNull PromelaInit o) {
    visitNavigablePsiElement(o);
  }

  public void visitInline(@NotNull PromelaInline o) {
    visitNamedElement(o);
  }

  public void visitInlineArg(@NotNull PromelaInlineArg o) {
    visitPsiVariable(o);
  }

  public void visitInlineCall(@NotNull PromelaInlineCall o) {
    visitNamedPsiElement(o);
  }

  public void visitIvar(@NotNull PromelaIvar o) {
    visitPsiVariable(o);
  }

  public void visitLabel(@NotNull PromelaLabel o) {
    visitNamedElement(o);
  }

  public void visitLen(@NotNull PromelaLen o) {
    visitPsiElement(o);
  }

  public void visitLenAnyExpression(@NotNull PromelaLenAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitLenBaseExpression(@NotNull PromelaLenBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitLtl(@NotNull PromelaLtl o) {
    visitNamedElement(o);
  }

  public void visitLtlBinBaseExpression(@NotNull PromelaLtlBinBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitLtlBody(@NotNull PromelaLtlBody o) {
    visitPsiElement(o);
  }

  public void visitLtlUnBaseExpression(@NotNull PromelaLtlUnBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitModules(@NotNull PromelaModules o) {
    visitPsiElement(o);
  }

  public void visitMtype(@NotNull PromelaMtype o) {
    visitPsiTypeDeclaration(o);
  }

  public void visitMtypeValue(@NotNull PromelaMtypeValue o) {
    visitPsiVariable(o);
  }

  public void visitMtypename(@NotNull PromelaMtypename o) {
    visitNamedPsiElement(o);
    // visitPsiElementWithType(o);
  }

  public void visitMultiplicationConstExpression(@NotNull PromelaMultiplicationConstExpression o) {
    visitConstExpression(o);
  }

  public void visitNever(@NotNull PromelaNever o) {
    visitNavigablePsiElement(o);
  }

  public void visitNoTrace(@NotNull PromelaNoTrace o) {
    visitPsiElement(o);
  }

  public void visitNpAnyExpression(@NotNull PromelaNpAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitNpBaseExpression(@NotNull PromelaNpBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitNumberRule(@NotNull PromelaNumberRule o) {
    visitConstantExpression(o);
  }

  public void visitOneDecl(@NotNull PromelaOneDecl o) {
    visitPsiElement(o);
  }

  public void visitOption(@NotNull PromelaOption o) {
    visitPsiElement(o);
  }

  public void visitParenthesis(@NotNull PromelaParenthesis o) {
    visitPsiElement(o);
  }

  public void visitPcValueAnyExpression(@NotNull PromelaPcValueAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitPcValueBaseExpression(@NotNull PromelaPcValueBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitPidReferAnyExpression(@NotNull PromelaPidReferAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitPidReferBaseExpression(@NotNull PromelaPidReferBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitPoll(@NotNull PromelaPoll o) {
    visitPsiElement(o);
  }

  public void visitPollAnyExpression(@NotNull PromelaPollAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitPollBaseExpression(@NotNull PromelaPollBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitPriority(@NotNull PromelaPriority o) {
    visitPsiElement(o);
  }

  public void visitProctype(@NotNull PromelaProctype o) {
    visitNamedElement(o);
  }

  public void visitProctypeHeader(@NotNull PromelaProctypeHeader o) {
    visitPsiElement(o);
  }

  public void visitPropertyAccess(@NotNull PromelaPropertyAccess o) {
    visitPsiElement(o);
  }

  public void visitRange(@NotNull PromelaRange o) {
    visitPsiElement(o);
  }

  public void visitReceive(@NotNull PromelaReceive o) {
    visitPsiElement(o);
  }

  public void visitRecvArg(@NotNull PromelaRecvArg o) {
    visitPsiElement(o);
  }

  public void visitRecvArgs(@NotNull PromelaRecvArgs o) {
    visitPsiElement(o);
  }

  public void visitRun(@NotNull PromelaRun o) {
    visitPsiElement(o);
  }

  public void visitRunAnyExpression(@NotNull PromelaRunAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitRunBaseExpression(@NotNull PromelaRunBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitSend(@NotNull PromelaSend o) {
    visitPsiElement(o);
  }

  public void visitSendArgs(@NotNull PromelaSendArgs o) {
    visitPsiElement(o);
  }

  public void visitSequence(@NotNull PromelaSequence o) {
    visitPsiElement(o);
  }

  public void visitSequenceBlock(@NotNull PromelaSequenceBlock o) {
    visitPsiElement(o);
  }

  public void visitSetPriorityAnyExpression(@NotNull PromelaSetPriorityAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitSetPriorityBaseExpression(@NotNull PromelaSetPriorityBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitSizeInitializer(@NotNull PromelaSizeInitializer o) {
    visitPsiElement(o);
  }

  public void visitStep(@NotNull PromelaStep o) {
    visitPsiElement(o);
  }

  public void visitStmnt(@NotNull PromelaStmnt o) {
    visitPsiElement(o);
  }

  public void visitTernaryAnyExpression(@NotNull PromelaTernaryAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitTernaryBaseExpression(@NotNull PromelaTernaryBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitTimeoutAnyExpression(@NotNull PromelaTimeoutAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitTimeoutBaseExpression(@NotNull PromelaTimeoutBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitTrace(@NotNull PromelaTrace o) {
    visitNavigablePsiElement(o);
  }

  public void visitTypename(@NotNull PromelaTypename o) {
    visitPsiElementWithType(o);
  }

  public void visitUnaryAnyExpression(@NotNull PromelaUnaryAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitUnaryBaseExpression(@NotNull PromelaUnaryBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitUnaryConstExpression(@NotNull PromelaUnaryConstExpression o) {
    visitConstExpression(o);
  }

  public void visitUnitConstExpression(@NotNull PromelaUnitConstExpression o) {
    visitConstExpression(o);
  }

  public void visitUnsignedDecl(@NotNull PromelaUnsignedDecl o) {
    visitPsiVariable(o);
  }

  public void visitUtype(@NotNull PromelaUtype o) {
    visitPsiTypeDeclaration(o);
  }

  public void visitVar(@NotNull PromelaVar o) {
    visitPsiVariableReferencingElement(o);
  }

  public void visitVarref(@NotNull PromelaVarref o) {
    visitPsiElement(o);
  }

  public void visitVarrefAnyExpression(@NotNull PromelaVarrefAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitVarrefBaseExpression(@NotNull PromelaVarrefBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitVisible(@NotNull PromelaVisible o) {
    visitPsiElement(o);
  }

  public void visitWrapAnyExpression(@NotNull PromelaWrapAnyExpression o) {
    visitAnyExpression(o);
  }

  public void visitWrapBaseExpression(@NotNull PromelaWrapBaseExpression o) {
    visitBaseExpression(o);
  }

  public void visitConstantExpression(@NotNull ConstantExpression o) {
    visitElement(o);
  }

  public void visitNamedPsiElement(@NotNull NamedPsiElement o) {
    visitElement(o);
  }

  public void visitNavigablePsiElement(@NotNull NavigablePsiElement o) {
    visitElement(o);
  }

  public void visitNamedElement(@NotNull PromelaNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElementWithType(@NotNull PsiElementWithType o) {
    visitElement(o);
  }

  public void visitPsiTypeDeclaration(@NotNull PsiTypeDeclaration o) {
    visitElement(o);
  }

  public void visitPsiVariable(@NotNull PsiVariable o) {
    visitElement(o);
  }

  public void visitPsiVariableReferencingElement(@NotNull PsiVariableReferencingElement o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
