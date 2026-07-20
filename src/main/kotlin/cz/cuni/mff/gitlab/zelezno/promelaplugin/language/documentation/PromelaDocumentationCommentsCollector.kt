package cz.cuni.mff.gitlab.zelezno.promelaplugin.language.documentation

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeafs
import com.intellij.psi.util.prevLeafs
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PromelaTokenSets
import cz.cuni.mff.gitlab.zelezno.promelaplugin.language.psi.PsiVariable
import cz.cuni.mff.gitlab.zelezno.promelaplugin.psi.PromelaTypes
import java.util.*

/**
 * Utility object for collecting documentation text from comments.
 */
object PromelaDocumentationCommentsCollector {
    private fun writeComment(el: PsiElement, builder: Appendable) {
        val content = when (el.elementType) {
            PromelaTypes.BLOCK_COMMENT -> el.text.run {
                subSequence(2, length - 2) // remove '/*' and '*/'
            }

            PromelaTypes.LINE_COMMENT -> el.text.run {
                subSequence(2, length) // remove '//'
            }

            else -> el.text // fallback, not really possible
        }
        builder.append(content)
    }

    /**
     * Collects comments for `
     *      // comment for x
     *      int x;`
     */
    private fun tryCollectingRightComments(el: PsiElement, builder: Appendable): Boolean {
        var written = false

        val elements = LinkedList<PsiElement>()
        var previous = el.prevLeafs
        while (true) {
            /*
             we only want to collect lines which do not contain other symbols. That means for variable x:

             Here 'value of x' is documentation of int x.
             // value of x
             int x;

             But here 'value of y' belongs to int y
             int y; // value of y
             int x;
             */
            val thisLine = LinkedList<PsiElement>()
            var takeThisLine = true
            var breakAfter = false
            var last: PsiElement? = null
            previous.takeWhile {
                if (it.elementType != TokenType.WHITE_SPACE) {
                    return@takeWhile true
                }
                // skip if there is at lease one blank line between comments
                val newlineCount = it.text.count { it == '\n' }
                if (newlineCount > 0) {
                    breakAfter = newlineCount > 1 // multiline white-space
                    last = it
                    false
                } else {
                    true
                }
            }.filter {
                it.elementType != TokenType.WHITE_SPACE
                        // ignore inserted semis, e.g. `;int value`
                        && !(it.elementType == PromelaTypes.SEMI && it.textLength == 0)
            }.
                // the first line of docs is the rightmost comment
            takeWhile {
                if (it.elementType in PromelaTokenSets.NORMAL_COMMENTS) {
                    true
                } else {
                    takeThisLine = false
                    false
                }
            }.forEach { thisLine.add(it) }
            if (takeThisLine) {
                elements.addAll(thisLine)
            } else {
                break
            }
            if (breakAfter) break
            previous = last?.prevLeafs ?: break
        }

        // descendingIterator as first comment must be written first
        elements.descendingIterator().forEach {
            written = true
            writeComment(it, builder)
        }

        return written
    }

    /**
     * Collects comments for `int x; // comment` or `int x; /* long
     *              comment line 1
     *              comment line 2
     * */`
     */
    private fun tryCollectingSameLineLeftComments(el: PsiElement, builder: Appendable): Boolean {
        var written = false
        el.nextLeafs.takeWhile {
            if (it.elementType != TokenType.WHITE_SPACE) {
                return@takeWhile true
            }
            !it.text.contains('\n') // stop at \n
        }.filter { it.elementType != TokenType.WHITE_SPACE && it.elementType != PromelaTypes.SEMI }
            .takeWhile { it.elementType in PromelaTokenSets.NORMAL_COMMENTS }
            .forEach {
                writeComment(it, builder)
                written = true
            }
        return written
    }

    fun findDocumentationText(el: PsiElement, builder: Appendable): Boolean {
        var findFor = el
        if (findFor is PsiVariable) {
            // variables are always defined inside some larger structure (oneDecl, mtype...)
            // we will take comments from it
            findFor = el.parent
        }
        if (tryCollectingSameLineLeftComments(findFor, builder)) return true
        return tryCollectingRightComments(findFor, builder)
    }
}