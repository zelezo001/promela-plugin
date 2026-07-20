// This is a generated file. Not intended for manual editing.
package cz.cuni.mff.gitlab.zelezno.promelaplugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaNamedElement;

public interface PromelaProctype extends PromelaNamedElement {

  @Nullable
  PromelaEnabler getEnabler();

  @Nullable
  PromelaIdentifier getIdentifier();

  @Nullable
  PromelaParenthesis getParenthesis();

  @Nullable
  PromelaPriority getPriority();

  @NotNull
  PromelaProctypeHeader getProctypeHeader();

  @Nullable
  PromelaSequenceBlock getSequenceBlock();

}
