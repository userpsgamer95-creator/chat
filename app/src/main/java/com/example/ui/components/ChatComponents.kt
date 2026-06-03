package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

// Premium HD image templates that users can send instantly without needing real storage loaded
val PRESETS_IMAGES_LIST = listOf(
    "https://images.unsplash.com/photo-1579546929518-9e396f3cc809?q=80&w=600&auto=format&fit=crop&ixlib=rb-4.0.3", // Nebula Liquid Gradient
    "https://images.unsplash.com/photo-1541701494587-cb58502866ab?q=80&w=600&auto=format&fit=crop&ixlib=rb-4.0.3", // Abstract Color Splash
    "https://images.unsplash.com/photo-1511447333015-45b65e60f6d5?q=80&w=600&auto=format&fit=crop&ixlib=rb-4.0.3", // Cyberpunk neon city lights
    "https://images.unsplash.com/photo-1470071459604-3b5ec3a7fe05?q=80&w=600&auto=format&fit=crop&ixlib=rb-4.0.3", // Soothing Natural Forest
    "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?q=80&w=600&auto=format&fit=crop&ixlib=rb-4.0.3", // Adorable Fluffy Cat
    "https://images.unsplash.com/photo-1513542789411-b6a5d4f31634?q=80&w=600&auto=format&fit=crop&ixlib=rb-4.0.3"  // Cool Abstract Art
)

@Composable
fun ChatWallpaperBackground(wallpaperId: Int, modifier: Modifier = Modifier) {
    when (wallpaperId) {
        0 -> { // Classic WhatsApp Soft Sage Grid
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFFE5DDD5))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val spacing = 45.dp.toPx()
                    val strokeColor = Color(0xFFDFD7CF)

                    // Draw vertical grid lines
                    var x = 0f
                    while (x < width) {
                        drawLine(
                            color = strokeColor,
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 1.dp.toPx()
                        )
                        x += spacing
                    }

                    // Draw horizontal grid lines
                    var y = 0f
                    while (y < height) {
                        drawLine(
                            color = strokeColor,
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                        y += spacing
                    }
                }
            }
        }
        1 -> { // Golden Sand Sunset Warm Flow
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFF9A9E),
                                Color(0xFFFECFEF),
                                Color(0xFFFE9A8B)
                            )
                        )
                    )
            )
        }
        2 -> { // Cyber Gothic Space Dark Grid
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFF0F172A))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val spacing = 60.dp.toPx()

                    // Horizontal glowing cosmic lines
                    var y = 0f
                    while (y < height) {
                        drawLine(
                            color = Color(0x3338BDF8),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1.2.dp.toPx()
                        )
                        y += spacing
                    }

                    // Vertical glowing lines
                    var x = 0f
                    while (x < width) {
                        drawLine(
                            color = Color(0x33C084FC),
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 1.2.dp.toPx()
                        )
                        x += spacing
                    }
                }
            }
        }
        3 -> { // Cozy Sage Organic Fog
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFEDE8D0),
                                Color(0xFFC7D3C1),
                                Color(0xFFA2B5A0)
                            )
                        )
                    )
            )
        }
        else -> {
            Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentBottomSheet(
    onDismiss: () -> Unit,
    onStoragePick: () -> Unit,
    onTemplateSelect: (String) -> Unit,
    onWallpaperStyleSelect: (Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "Premium Sharing Panel",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Direct Storage Photo Selector
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStoragePick() }
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Pick storage", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Pick from Device Storage", fontWeight = FontWeight.Bold)
                        Text("Access your actual storage photos securely", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Beautiful Wallpaper Selector Strip
            Text(
                text = "🎨 Match Chat Wallpaper",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Pre-populated preset styles
                val wallpaperNames = listOf("Classic", "Sunset", "Gothic", "Forest")
                val wallpaperColors = listOf(Color(0xFFE5DDD5), Color(0xFFFF9A9E), Color(0xFF0F172A), Color(0xFFEDE8D0))
                
                wallpaperNames.forEachIndexed { index, name ->
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(64.dp)
                            .clickable { onWallpaperStyleSelect(index) },
                        colors = CardDefaults.cardColors(containerColor = wallpaperColors[index]),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (index == 2) Color.White else Color.Black,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Instant HD Pre-loaded Presets Gallery (Incredibly useful on simulated emulators)
            Text(
                text = "✨ Instant HD Presets (Perfect for Emulators!)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(140.dp)
            ) {
                items(PRESETS_IMAGES_LIST) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Simulated photos",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onTemplateSelect(imageUrl) }
                    )
                }
            }
        }
    }
}
