// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PromelaVarref extends PsiElement {

  @Nullable
  PromelaIndexer getIndexer();

  @NotNull
  List<PromelaPropertyAccess> getPropertyAccessList();

  @NotNull
  PromelaVar getVar();

}
