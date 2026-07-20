// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PromelaModules extends PsiElement {

  @NotNull
  List<PromelaInit> getInitList();

  @NotNull
  List<PromelaInline> getInlineList();

  @NotNull
  List<PromelaLtl> getLtlList();

  @NotNull
  List<PromelaMtype> getMtypeList();

  @NotNull
  List<PromelaNever> getNeverList();

  @NotNull
  List<PromelaOneDecl> getOneDeclList();

  @NotNull
  List<PromelaProctype> getProctypeList();

  @NotNull
  List<PromelaTrace> getTraceList();

  @NotNull
  List<PromelaUtype> getUtypeList();

}
