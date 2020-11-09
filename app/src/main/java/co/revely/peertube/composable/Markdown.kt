package co.revely.peertube.composable

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.platform.UriHandlerAmbient
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import co.revely.peertube.R
import dev.chrisbanes.accompanist.coil.CoilImage
import org.commonmark.node.*
import org.commonmark.node.Paragraph
import org.commonmark.parser.Parser
import org.intellij.lang.annotations.Language

/**
 * These functions will render a tree of Markdown nodes parsed with CommonMark.
 * Images will be rendered using Chris Banes Accompanist library (which uses Coil)
 *
 * To use this, you need the following two dependencies:
 * implementation "com.atlassian.commonmark:commonmark:0.15.2"
 * implementation "dev.chrisbanes.accompanist:accompanist-coil:0.2.0"
 *
 * The following is an example of how to use this:
 * ```
 * val parser = Parser.builder().build()
 * val root = parser.parse(MIXED_MD) as Document
 * val markdownComposer = MarkdownComposer()
 *
 * MarkdownComposerTheme {
 *    MDDocument(root)
 * }
 * ```
 */
private const val TAG_URL = "url"
private const val TAG_IMAGE_URL = "imageUrl"

private val parser = Parser.builder().build()

@Composable
fun Markdown(text: String, modifier: Modifier = Modifier)
{
	MDDocument(parser.parse(text) as Document, modifier)
}

@Composable
fun MDDocument(document: Document, modifier: Modifier = Modifier) {
	Column(modifier = modifier) {
		MDBlockChildren(document)
	}
}

@Composable
fun MDHeading(heading: Heading, modifier: Modifier = Modifier) {
	val style = when (heading.level) {
		1 -> PeertubeTheme.typography.h1
		2 -> PeertubeTheme.typography.h2
		3 -> PeertubeTheme.typography.h3
		4 -> PeertubeTheme.typography.h4
		5 -> PeertubeTheme.typography.h5
		6 -> PeertubeTheme.typography.h6
		else -> {
			// Invalid header...
			MDBlockChildren(heading)
			return
		}
	}

	val padding = if (heading.parent is Document) 8.dp else 0.dp
	Box(modifier.padding(bottom = padding)) {
		val text = annotatedString {
			appendMarkdownChildren(heading, PeertubeTheme.colors)
		}
		MarkdownText(text, style)
	}
}

@Composable
fun MDParagraph(paragraph: Paragraph, modifier: Modifier = Modifier) {
	if (paragraph.firstChild is Image && paragraph.firstChild == paragraph.lastChild) {
		// Paragraph with single image
		MDImage(paragraph.firstChild as Image, modifier)
	} else {
		val padding = if (paragraph.parent is Document) 8.dp else 0.dp
		Box(modifier = modifier.padding(bottom = padding)) {
			val styledText = annotatedString {
				pushStyle(PeertubeTheme.typography.body1.toSpanStyle())
				appendMarkdownChildren(paragraph, PeertubeTheme.colors)
				pop()
			}
			MarkdownText(styledText, PeertubeTheme.typography.body1)
		}
	}
}

@Composable
fun MDImage(image: Image, modifier: Modifier = Modifier) {
	Box(modifier = modifier.fillMaxWidth(), alignment = Alignment.Center) {
		CoilImage(image.destination, modifier)
	}
}

@Composable
fun MDBulletList(bulletList: BulletList, modifier: Modifier = Modifier) {
	val marker = bulletList.bulletMarker
	MDListItems(bulletList, modifier = modifier) {
		val text = annotatedString {
			pushStyle(PeertubeTheme.typography.body1.toSpanStyle())
			append("$marker ")
			appendMarkdownChildren(it, PeertubeTheme.colors)
			pop()
		}
		MarkdownText(text, PeertubeTheme.typography.body1, modifier)
	}
}

@Composable
fun MDOrderedList(orderedList: OrderedList, modifier: Modifier = Modifier) {
	var number = orderedList.startNumber
	val delimiter = orderedList.delimiter
	MDListItems(orderedList, modifier) {
		val text = annotatedString {
			pushStyle(PeertubeTheme.typography.body1.toSpanStyle())
			append("${number++}$delimiter ")
			appendMarkdownChildren(it, PeertubeTheme.colors)
			pop()
		}
		MarkdownText(text, PeertubeTheme.typography.body1, modifier)
	}
}

@Composable
fun MDListItems(
	listBlock: ListBlock,
	modifier: Modifier = Modifier,
	item: @Composable (node: Node) -> Unit
) {
	val bottom = if (listBlock.parent is Document) 8.dp else 0.dp
	val start = if (listBlock.parent is Document) 0.dp else 8.dp
	Column(modifier = modifier.padding(start = start, bottom = bottom)) {
		var listItem = listBlock.firstChild
		while (listItem != null) {
			var child = listItem.firstChild
			while (child != null) {
				when (child) {
					is BulletList -> MDBulletList(child, modifier)
					is OrderedList -> MDOrderedList(child, modifier)
					else -> item(child)
				}
				child = child.next
			}
			listItem = listItem.next
		}
	}
}

@Composable
fun MDBlockQuote(blockQuote: BlockQuote, modifier: Modifier = Modifier) {
	val color = PeertubeTheme.colors.onBackground
	Box(modifier = modifier.drawBehind {
		drawLine(
			color = color,
			strokeWidth = 2f,
			start = Offset(12.dp.value, 0f),
			end = Offset(12.dp.value, size.height)
		)
	}.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)) {
		val text = annotatedString {
			pushStyle(
				PeertubeTheme.typography.body1.toSpanStyle()
					.plus(SpanStyle(fontStyle = FontStyle.Italic))
			)
			appendMarkdownChildren(blockQuote, PeertubeTheme.colors)
			pop()
		}
		Text(text, modifier)
	}
}

@Composable
fun MDFencedCodeBlock(fencedCodeBlock: FencedCodeBlock, modifier: Modifier = Modifier) {
	val padding = if (fencedCodeBlock.parent is Document) 8.dp else 0.dp
	Box(modifier = modifier.padding(bottom = padding, start = 8.dp)) {
		Text(
			text = fencedCodeBlock.literal,
			style = TextStyle(fontFamily = FontFamily.Monospace),
			modifier = modifier
		)
	}
}

@Composable
fun MDIndentedCodeBlock(indentedCodeBlock: IndentedCodeBlock, modifier: Modifier = Modifier) {
	// Ignored
}

@Composable
fun MDThematicBreak(thematicBreak: ThematicBreak, modifier: Modifier = Modifier) {
	//Ignored
}

@Composable
fun MDHtml(htmlBlock: HtmlBlock, modifier: Modifier = Modifier) {
	val uriHandler = UriHandlerAmbient.current
	Html(html = htmlBlock.literal) {
		uriHandler.openUri(it)
	}
}

@Composable
fun MDBlockChildren(parent: Node) {
	var child = parent.firstChild
	while (child != null) {
		when (child) {
			is BlockQuote -> MDBlockQuote(child)
			is ThematicBreak -> MDThematicBreak(child)
			is Heading -> MDHeading(child)
			is Paragraph -> MDParagraph(child)
			is FencedCodeBlock -> MDFencedCodeBlock(child)
			is IndentedCodeBlock -> MDIndentedCodeBlock(child)
			is Image -> MDImage(child)
			is BulletList -> MDBulletList(child)
			is OrderedList -> MDOrderedList(child)
			is HtmlBlock -> MDHtml(child)
		}
		child = child.next
	}
}

fun AnnotatedString.Builder.appendMarkdownChildren(
	parent: Node, colors: Colors
) {
	var child = parent.firstChild
	while (child != null) {
		when (child) {
			is Paragraph -> appendMarkdownChildren(child, colors)
			is Text -> append(child.literal)
			is Image -> appendInlineContent(TAG_IMAGE_URL, child.destination)
			is Emphasis -> {
				pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
				appendMarkdownChildren(child, colors)
				pop()
			}
			is StrongEmphasis -> {
				pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
				appendMarkdownChildren(child, colors)
				pop()
			}
			is Code -> {
				pushStyle(TextStyle(fontFamily = FontFamily.Monospace).toSpanStyle())
				append(child.literal)
				pop()
			}
			is SoftLineBreak -> append("\n")
			is HardLineBreak -> append("\n")
			is Link -> {
				val underline = SpanStyle(textDecoration = TextDecoration.Underline)
				pushStyle(underline)
				pushStringAnnotation(TAG_URL, child.destination)
				appendMarkdownChildren(child, colors)
				pop()
				pop()
			}
		}
		child = child.next
	}
}

@Composable
fun MarkdownText(text: AnnotatedString, style: TextStyle, modifier: Modifier = Modifier) {
	val uriHandler = UriHandlerAmbient.current
	val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

	Text(text = text,
		modifier = modifier.tapGestureFilter { pos ->
			layoutResult.value?.let { layoutResult ->
				val position = layoutResult.getOffsetForPosition(pos)
				text.getStringAnnotations(position, position)
					.firstOrNull()
					?.let { sa ->
						if (sa.tag == TAG_URL) {
							uriHandler.openUri(sa.item)
						}
					}
			}
		},
		style = style,
		inlineContent = mapOf(
			TAG_IMAGE_URL to InlineTextContent(
				Placeholder(style.fontSize, style.fontSize, PlaceholderVerticalAlign.Bottom)
			) {
				CoilImage(it, alignment = Alignment.Center)
			}
		),
		onTextLayout = { layoutResult.value = it }
	)
}

@Preview(showBackground = true)
@Composable
fun MarkdownPreview()
{
	val parser = Parser.builder().build()
	val root = parser.parse(MIXED_MD) as Document
	PeertubeTheme {
		ScrollableColumn(
			modifier = Modifier.background(PeertubeTheme.colors.background)
		) {
			MDDocument(root)
		}
	}
}

@Language("Markdown")
const val MIXED_MD = """
### Markdown Header
This is regular text without formatting in a single paragraph.
![Serious](drawable://${R.drawable.logo})
Images can also be inline: ![Serious](drawable://${R.drawable.logo}). [Links](http://hellsoft.se) and `inline code` also work. This *is* text __with__ inline styles for *__bold and italic__*. Those can be nested.
Here is a code block:
```javascript
function codeBlock() {
    return true;
}
```
+ Bullet
+ __Lists__
+ Are
+ *Cool*
1. **First**
1. *Second*
1. Third
1. [Fourth is clickable](https://google.com)  
   1. And
   1. Sublists
1. Mixed
   - With
   - Bullet
   - Lists
100) Lists
100) Can
100) Have
100) *Custom*
100) __Start__
100) Numbers
- List
- Of
- Items
  - With
  - Sublist
> A blockquote is useful for quotes!
"""