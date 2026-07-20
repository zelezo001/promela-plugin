package cz.cuni.mff.gitlab.zelezno.promelaplugin.build

import com.intellij.compiler.impl.BuildTargetScopeProvider
import com.intellij.compiler.impl.CompileScopeUtil
import com.intellij.compiler.impl.OneProjectItemCompileScope
import com.intellij.openapi.compiler.CompileScope
import com.intellij.openapi.compiler.CompilerManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import cz.cuni.mff.gitlab.zelezno.promelaplugin.verification.VerificationRunConfiguration
import org.jetbrains.jps.api.CmdlineRemoteProto.Message.ControllerMessage.ParametersMessage.TargetTypeBuildScope

private const val PROMELA_VERIFICATION_ID = "promela-verification"
private const val PROMELA_STATE_TABLE_ID = "promela-state-table"

/**
 * Provider for building plain/verification models in Promela.
 */
class PromelaBuildTargetScopeProvider : BuildTargetScopeProvider() {

    /**
     * Adds build targets for building plain/verification models
     */
    override fun getBuildTargetScopes(
        baseScope: CompileScope, project: Project, forceBuild: Boolean
    ): List<TargetTypeBuildScope?> {
        val scopes: MutableList<TargetTypeBuildScope?> = mutableListOf()

        baseScope.getUserData(CompilerManager.RUN_CONFIGURATION_KEY)?.also {
            if (it is VerificationRunConfiguration) {
                scopes.add(
                    TargetTypeBuildScope.newBuilder()
                        .setForceBuild(true) // always rebuild do to possible includes
                        .setTypeId(PROMELA_VERIFICATION_ID)
                        .addTargetId(promelaVerificationTargetId(it)).build()
                )
            }
        }

        return scopes
    }
}

private fun promelaVerificationTargetId(run: VerificationRunConfiguration): String {
    return "promela-verification://${run.name}"
}

private fun promelaStateTableTargetId(file: VirtualFile): String {
    return "promela://${file.path}"
}

private fun createStateTableTargetTypeBuildScope(file: VirtualFile): TargetTypeBuildScope {
    val builder = TargetTypeBuildScope.newBuilder().setTypeId(PROMELA_STATE_TABLE_ID)
    return builder
        .setForceBuild(true) // always rebuild do to possible includes
        .addTargetId(promelaStateTableTargetId(file))
        .build()
}

/**
 * Create a compile scope for building a state table model. File must be in the local file system.
 * @param project the current project
 * @param file the file for which to build the state table
 * @return the created compile scope
 */
fun buildStateTableBuildScope(project: Project, file: VirtualFile): CompileScope {
    val scope = OneProjectItemCompileScope(project, file)
    // set directly what we want to build as otherwise all targets connected to the file (in current state this would probably work correctly)
    CompileScopeUtil.setBaseScopeForExternalBuild(scope, listOf(createStateTableTargetTypeBuildScope(file)))
    return scope
}