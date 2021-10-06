package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.place

import android.net.Uri
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.composables.ComposeExoPlayer
import com.inmersoft.trinidadpatrimonial.composables.ComposePanoView
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.extensions.smartTruncate
import com.inmersoft.trinidadpatrimonial.ui.loader.ui.theme.TrinidadPatrimonialTheme
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.placeholderList
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@ExperimentalAnimationApi
@AndroidEntryPoint
class PlaceContainerDetailsFragment : Fragment() {

    private val safeArgs: PlaceContainerDetailsFragmentArgs by navArgs()
    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private lateinit var playerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    var currentWindows = 0
    var playbackPosition = 0L
    var playWhenReady = false

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            TrinidadPatrimonialTheme {
                ProvideWindowInsets {
                    PlaceDetailScreen(requireActivity(), trinidadDataViewModel.allPlaces)
                }
            }
        }

    }


    @ExperimentalMaterialApi
    @Composable
    fun PlaceDetailScreen(context: FragmentActivity, placesLiveData: LiveData<List<Place>>) {
        val placesData by placesLiveData.observeAsState(initial = emptyList())
        val userSelectPlaceId = safeArgs.placeID
        val currentPlace = placesData.firstOrNull() { place -> place.place_id == userSelectPlaceId }
        if (currentPlace != null)
            PlaceDetailsContent(context, placesData, currentPlace = currentPlace)
        else
            ShowPlaceHolder()
    }

    @Composable
    private fun ShowPlaceHolder() {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .placeholder(
                        color = Color.LightGray,
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                    )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .placeholder(
                        color = Color.LightGray,
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                    )
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .placeholder(
                                color = Color.LightGray,
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                            )
                            .padding(10.dp)


                    )
                }
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun PlaceDetailsContent(
        context: FragmentActivity,
        placesData: List<Place>,
        currentPlace: Place
    ) {

        BackdropScaffold(
            backLayerBackgroundColor = MaterialTheme.colors.background,
            scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
            appBar = {

            },
            frontLayerScrimColor = Color.Unspecified,
            backLayerContent = {
                PlaceBanner(
                    TrinidadAssets.getAssetFullPath(
                        currentPlace.header_images[0],
                        TrinidadAssets.jpg
                    ),
                    Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                )

            },
            frontLayerContent = {
                PlaceSections(
                    context,
                    modifier = Modifier.fillMaxSize(),
                    currentPlace,
                    placesData
                )
            })

    }

    @ExperimentalAnimationApi
    @Composable
    fun PlaceSections(
        context: FragmentActivity,
        modifier: Modifier = Modifier,
        currentPlace: Place,
        placesData: List<Place>,
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colors.surface,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
        ) {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                ) {

                    item {
                        PlacesDescription(
                            currentPlace.place_name,
                            currentPlace.place_description
                        )
                    }

                    if (currentPlace.pano[0].isNotEmpty()) {
                        item {
                            val panoUrl = TrinidadAssets.getAssetFullPath(
                                currentPlace.pano[0],
                                TrinidadAssets.webp
                            )
                            PlacePano360(context, Uri.parse(panoUrl))
                        }
                    }
                    if (currentPlace.video_promo.isNotEmpty()) {
                        item {
                            PlacesVideo(currentPlace.video_promo)
                        }
                    }
                    item {
                        OtherPlaces(placesData)
                    }
                    item {
                        Spacer(modifier = Modifier.padding(20.dp))
                    }
                }
            }
        }
    }


    @Composable
    fun OtherPlaces(placesList: List<Place>) {
        Card(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.others_places),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    items(placesList) { place ->
                        OtherPlaceItem(
                            TrinidadAssets.getAssetFullPath(
                                place.header_images[0],
                                TrinidadAssets.jpg
                            ), place.place_name
                        )
                    }
                }

            }
        }

    }


    @Composable
    fun OtherPlaceItem(imageUrl: String, placeName: String) {
        Card(
            elevation = 4.dp,
            modifier = Modifier
                .height(150.dp)
                .width(180.dp)
                .padding(start = 4.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .clickable {

                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                val imagePainter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        crossfade(true)
                        error(placeholderList[Random.nextInt(placeholderList.size)])
                    }
                )

                val loadingImageState = imagePainter.state is ImagePainter.State.Loading

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .placeholder(
                            color = Color.LightGray,
                            visible = loadingImageState,
                            highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White)
                        ),
                    contentScale = ContentScale.Crop,
                    contentDescription = "OtherPlaces",
                    painter = imagePainter
                )
                Text(
                    text = placeName,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Spacer(Modifier.height(5.dp))
            }
        }
    }

    @Composable
    private fun PlacesVideo(videoPromo: String) {
        Card(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                Text(
                    text = "Video",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 4.dp)
                )
                VideoPlayer(videoPromo = videoPromo)
            }

        }
    }

    @Composable
    private fun PlacePano360(context: FragmentActivity, imageUrl: Uri) {
        Card(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = stringResource(R.string.imagen360),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,


                    )
                Spacer(Modifier.height(20.dp))
                ComposePanoView(
                    context,
                    panoUri = imageUrl,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                )
            }
        }
    }

    @ExperimentalAnimationApi
    @Composable
    private fun PlacesDescription(placeName: String, placeDescription: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp, bottom = 8.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(10.dp)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = placeName,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.padding(8.dp))
                AnimatedContent(
                    targetState = expanded,
                    transitionSpec = {
                        // Compare the incoming number with the previous number.
                        if (expanded) {
                            // If the target number is larger, it slides up and fades in
                            // while the initial (smaller) number slides up and fades out.
                            fadeIn() with
                                    fadeOut()
                        } else {
                            // If the target number is smaller, it slides down and fades in
                            // while the initial number slides down and fades out.
                            fadeIn() with
                                    fadeOut()
                        }.using(
                            // Disable clipping since the faded slide-in/out should
                            // be displayed out of bounds.
                            SizeTransform(clip = false)
                        )
                    }
                ) { targetExpanded ->
                    Text(
                        text = if (targetExpanded) placeDescription else placeDescription.smartTruncate(),
                        textAlign = TextAlign.Justify,
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (!expanded) Icons.Filled.Add else Icons.Filled.Remove,
                        contentDescription = ""
                    )
                }
            }
        }
    }


    @Composable
    private fun PlacesActionButtons() {
        Card(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(R.color.trinidadColorPrimary),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.ic_round_directions_24),
                        contentDescription = "button description"
                    )
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    border = BorderStroke(1.dp, colorResource(R.color.trinidadColorPrimary)),
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.trinidadColorPrimary))
                ) {
                    Icon(
                        painterResource(R.drawable.ic_baseline_hearing_24),
                        contentDescription = "button description"
                    )
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    border = BorderStroke(1.dp, colorResource(R.color.trinidadColorPrimary)),
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.trinidadColorPrimary))
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "button description"
                    )
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    border = BorderStroke(1.dp, colorResource(R.color.trinidadColorPrimary)),
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors =
                    ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.trinidadColorPrimary))
                ) {
                    Icon(
                        painterResource(R.drawable.ic_baseline_link_24),
                        contentDescription = "button description"
                    )
                }
            }

        }
    }

    @Composable
    fun PlaceBanner(imageUrl: String, modifier: Modifier = Modifier) {
        Column {
            Image(
                modifier = modifier,
                contentScale = ContentScale.Crop,
                contentDescription = "",
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.placeholder_1)
                    }
                )
            )
            PlacesActionButtons()
            Spacer(Modifier.height(8.dp))
        }
    }

    @Composable
    fun VideoPlayer(videoPromo: String) {
        val context = LocalContext.current
        simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        val playerView = PlayerView(context).apply {
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        }
        val playWhenReady by remember {
            mutableStateOf(true)
        }

        playYoutubeUrl(videoPromo)

        playerView.player = simpleExoPlayer
        LaunchedEffect(simpleExoPlayer) {
            simpleExoPlayer.prepare()
            simpleExoPlayer.playWhenReady = playWhenReady
        }
        AndroidView(modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .clip(shape = RoundedCornerShape(20.dp)), factory = {
            playerView
        })
    }

    private fun playYoutubeUrl(videoPromo: String) {
        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?,
            ) {
                if (ytFiles != null) {
                    val videoTag = 137 //Tag 1080
                    val audioTag = 140 //Tag 1080
                    val audioSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(ytFiles.get(audioTag).url))
                    val videoSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(ytFiles.get(videoTag).url))

                    simpleExoPlayer.setMediaSource(
                        MergingMediaSource(
                            true,
                            videoSource,
                            audioSource
                        ),
                        true
                    )
                    simpleExoPlayer.prepare()
                    simpleExoPlayer.playWhenReady = false
                    simpleExoPlayer.seekTo(currentWindows, playbackPosition)

                }
            }

        }.extract(videoPromo)
    }

}




