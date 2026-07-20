// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PromelaStep extends PsiElement {

  @NotNull
  List<PromelaOneDecl> getOneDeclList();

  @NotNull
  List<PromelaStmnt> getStmntList();

  @NotNull
  List<PromelaVarref> getVarrefList();

}
