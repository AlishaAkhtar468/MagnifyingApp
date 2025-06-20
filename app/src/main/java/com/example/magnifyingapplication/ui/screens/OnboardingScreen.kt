package com.example.magnifyingapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magnifyingapplication.ui.theme.OnboardingDescriptionStyle
import com.example.magnifyingapplication.ui.theme.OnboardingHeadingStyle
import com.example.magnifyingapplication.ui.theme.SkipTextStyle
import com.example.magnifyingapplication.ui.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import com.example.magnifyingapplication.R
import androidx.compose.ui.layout.ContentScale

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = viewModel(),
    onFinish: () -> Unit,
    onSkip: () -> Unit
) {
    val onboardingItems = viewModel.onboardingItems
    val pagerState = rememberPagerState(initialPage = viewModel.currentPage.collectAsState().value)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                count = onboardingItems.size,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) { page ->
                val item = onboardingItems[page]

                // Background image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.magnifier),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = null,
                            modifier = Modifier
                                .height(320.dp)
                                .padding(bottom = 32.dp)
                        )

                        Text(
                            text = item.title,
                            style = OnboardingHeadingStyle()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = item.description,
                            style = OnboardingDescriptionStyle(),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = {
                    viewModel.skip()
                    onSkip() // Navigate to Camera screen
                }) {
                    Text(
                        text = "Skip",
                        style = SkipTextStyle()
                    )
                }

                Image(
                    painter = painterResource(id = onboardingItems[pagerState.currentPage].nextIconRes),
                    contentDescription = "Next",
                    modifier = Modifier
                        .size(56.dp)
                        .clickable {
                            coroutineScope.launch {
                                if (pagerState.currentPage < onboardingItems.lastIndex) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                    viewModel.nextPage()
                                } else {
                                    onFinish()
                                }
                            }
                        }
                )
            }
        }
    }
}
