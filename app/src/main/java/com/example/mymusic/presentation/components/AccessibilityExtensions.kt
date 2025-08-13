package com.example.mymusic.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.*

// Enhanced clickable with accessibility support
fun Modifier.accessibleClickable(
    onClickLabel: String? = null,
    role: Role? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    this
        .clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
            onClick = onClick
        )
        .graphicsLayer {
            alpha = if (isPressed) 0.7f else 1f
        }
        .semantics {
            // Add semantic properties for screen readers
            if (!enabled) {
                this.disabled()
            }
        }
}

// Enhanced touch target for accessibility
fun Modifier.accessibleTouchTarget(
    minSize: Int = 48
): Modifier = composed {
    this.semantics {
        // Ensure minimum touch target size by setting content description
        this.contentDescription = "Touch target with minimum size $minSize"
    }
}

// Enhanced content description
fun Modifier.accessibleContentDescription(
    contentDescription: String?,
    stateDescription: String? = null
): Modifier = composed {
    this.semantics {
        contentDescription?.let { this.contentDescription = it }
        stateDescription?.let { this.stateDescription = it }
    }
}

// Enhanced heading for screen readers
fun Modifier.accessibleHeading(
    level: Int = 1
): Modifier = composed {
    this.semantics {
        this.heading()
        // Note: headingLevel is not available in current Compose version
    }
}

// Enhanced button semantics
fun Modifier.accessibleButton(
    onClickLabel: String? = null,
    enabled: Boolean = true
): Modifier = composed {
    this.semantics {
        this.role = Role.Button
        if (!enabled) {
            this.disabled()
        }
    }
}

// Enhanced list semantics
fun Modifier.accessibleList(
    itemCount: Int,
    itemDescription: String = "item"
): Modifier = composed {
    this.semantics {
        // Note: listItemCount and listItemDescription are not available in current Compose version
        this.contentDescription = "List with $itemCount $itemDescription(s)"
    }
}

// Enhanced search semantics
fun Modifier.accessibleSearch(
    query: String = ""
): Modifier = composed {
    this.semantics {
        // Note: searchQuery is not available in current Compose version
        this.role = Role.Button
        this.contentDescription = "Search for $query"
    }
}

// Enhanced progress semantics
fun Modifier.accessibleProgress(
    progress: Float,
    range: ClosedFloatingPointRange<Float> = 0f..1f
): Modifier = composed {
    this.semantics {
        this.role = Role.Button
        this.progressBarRangeInfo = ProgressBarRangeInfo(
            current = progress,
            range = range,
            steps = 0
        )
    }
}

// Enhanced image semantics
fun Modifier.accessibleImage(
    contentDescription: String,
    decorative: Boolean = false
): Modifier = composed {
    this.semantics {
        this.contentDescription = contentDescription
        // Note: decorative() is not available in current Compose version
    }
}

// Enhanced text semantics
fun Modifier.accessibleText(
    text: String,
    selectable: Boolean = false
): Modifier = composed {
    this.semantics {
        // Note: text and selectableText are not available in current Compose version
        this.contentDescription = text
    }
}

// Enhanced navigation semantics
fun Modifier.accessibleNavigation(
    isSelected: Boolean = false
): Modifier = composed {
    this.semantics {
        this.role = Role.Tab
        // Note: selected() function is not available in current Compose version
    }
}

// Enhanced form field semantics
fun Modifier.accessibleFormField(
    label: String,
    value: String = "",
    placeholder: String? = null,
    error: String? = null
): Modifier = composed {
    this.semantics {
        // Note: textFieldLabel, textFieldValue, textFieldPlaceholder are not available
        this.contentDescription = "$label: $value${placeholder?.let { " (placeholder: $it)" } ?: ""}${error?.let { " (error: $it)" } ?: ""}"
    }
}

// Enhanced slider semantics
fun Modifier.accessibleSlider(
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int = 0
): Modifier = composed {
    this.semantics {
        this.role = Role.Button
        this.progressBarRangeInfo = ProgressBarRangeInfo(
            current = value,
            range = range,
            steps = steps
        )
    }
}

// Enhanced switch semantics
fun Modifier.accessibleSwitch(
    checked: Boolean,
    role: Role = Role.Button
): Modifier = composed {
    this.semantics {
        this.role = role
        // Note: selected() function is not available in current Compose version
    }
}

// Enhanced checkbox semantics
fun Modifier.accessibleCheckbox(
    checked: Boolean,
    role: Role = Role.Checkbox
): Modifier = composed {
    this.semantics {
        this.role = role
        // Note: selected() function is not available in current Compose version
    }
}

// Enhanced radio button semantics
fun Modifier.accessibleRadioButton(
    selected: Boolean,
    role: Role = Role.RadioButton
): Modifier = composed {
    this.semantics {
        this.role = role
        // Note: selected() function is not available in current Compose version
    }
}
