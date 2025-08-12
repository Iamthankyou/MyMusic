package com.example.mymusic.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign

/**
 * Extension function to add accessibility support to clickable elements
 */
fun Modifier.accessibleClickable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    label: String? = null,
    role: Role? = null
): Modifier {
    return this
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            enabled = enabled,
            onClickLabel = label,
            role = role,
            onClick = onClick
        )
        .semantics {
            label?.let { contentDescription = it }
        }
}

/**
 * Extension function to add heading semantics
 */
fun Modifier.heading(): Modifier {
    return this.semantics {
        heading()
    }
}

/**
 * Extension function to add content description
 */
fun Modifier.contentDescription(description: String): Modifier {
    return this.semantics {
        contentDescription = description
    }
}

/**
 * Composable for accessible text with proper semantics
 */
@Composable
fun AccessibleText(
    text: String,
    modifier: Modifier = Modifier,
    isHeading: Boolean = false,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier.then(
            if (isHeading) Modifier.heading() else Modifier
        ),
        textAlign = textAlign,
        style = MaterialTheme.typography.bodyMedium
    )
}

/**
 * Composable for accessible button with proper feedback
 */
@Composable
fun AccessibleButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .scale(if (isPressed) 0.95f else 1f)
            .contentDescription(label),
        interactionSource = interactionSource
    ) {
        content()
    }
}

/**
 * Composable for accessible card with proper semantics
 */
@Composable
fun AccessibleCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    label: String? = null,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.Card(
        modifier = modifier.then(
            if (onClick != null && label != null) {
                Modifier.accessibleClickable(
                    onClick = onClick,
                    label = label,
                    role = Role.Button
                )
            } else {
                Modifier
            }
        )
    ) {
        content()
    }
}
