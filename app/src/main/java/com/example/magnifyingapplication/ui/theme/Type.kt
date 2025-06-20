package com.example.magnifyingapplication.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import com.example.magnifyingapplication.R
import com.example.magnifyingapplication.ui.theme.*


val NunitoSans = FontFamily(
    Font(R.font.nunito_sans_variable, FontWeight.Normal),
    Font(R.font.nunito_sans_bold, FontWeight.Bold)
)


val DMSans = FontFamily(
    Font(R.font.dm_sans_variable, FontWeight.Normal),
    Font(R.font.dm_sans_variable, FontWeight.Medium),
    Font(R.font.dm_sans_variable, FontWeight.Bold),
    Font(R.font.dm_sans_variable, FontWeight.Black),
    Font(R.font.dm_sans_variable_italic, FontWeight.Normal)
)

@Composable
fun headingTextStyle(): TextStyle {
    return TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}


@Composable
fun BodyTextStyle(): TextStyle {
    return TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}
@Composable
fun SkipTextStyle(): TextStyle {
    return TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Black,
        fontSize = 15.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground

    )
}

@Composable
fun OnboardingHeadingStyle(): TextStyle {
    return TextStyle(
        fontFamily = NunitoSans,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground

    )
}

@Composable
fun OnboardingDescriptionStyle(): TextStyle {
    return TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.3).sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onBackground
    )
}