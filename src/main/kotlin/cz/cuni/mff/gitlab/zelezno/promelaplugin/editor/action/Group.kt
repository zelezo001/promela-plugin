package cz.cuni.mff.gitlab.zelezno.promelaplugin.editor.action

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager

private const val AUTOMATON_ACTION_GROUP = "Promela.AutomatonView"

/**
 * @return Group of actions linked to state automata view
 */
fun ActionManager.getAutomatonActionGroup(): ActionGroup {
    return getAction(AUTOMATON_ACTION_GROUP) as ActionGroup
}