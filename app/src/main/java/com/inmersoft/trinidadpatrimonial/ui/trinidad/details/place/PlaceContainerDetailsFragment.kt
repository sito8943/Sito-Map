package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.place

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsHeight
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
import com.inmersoft.trinidadpatrimonial.composables.ComposePanoView
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.extensions.smartTruncate
import com.inmersoft.trinidadpatrimonial.ui.loader.ui.theme.TrinidadPatrimonialTheme
import com.inmersoft.trinidadpatrimonial.utils.*
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.random.Random

@ExperimentalAnimationApi
@AndroidEntryPoint
class PlaceContainerDetailsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val safeArgs: PlaceContainerDetailsFragmentArgs by navArgs()
    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    private var currentLocale = Locale("es", "ES")

    private lateinit var textToSpeechEngine: TextToSpeech

    private var currentGlobalPlace: Place? = null

    private var canSpeak = true

    private lateinit var playerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    var currentWindows = 0
    var playbackPosition = 0L
    var playWhenReady = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textToSpeechEngine = TextToSpeech(requireActivity()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                if (textToSpeechEngine.isLanguageAvailable(
                        currentLocale
                    ) == TextToSpeech.LANG_AVAILABLE
                ) {
                    textToSpeechEngine.language = currentLocale
                } else {
                    canSpeak = false
                }
            }
        }
    }


    private fun goToMap(placeId: Int) {
        val action =
            PlaceContainerDetailsFragmentDirections.actionDetailsFragmentToNavMap(placeID = placeId)
        findNavController().navigate(action)
    }


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
        val place = placesData.firstOrNull() { place -> place.place_id == userSelectPlaceId }
        val currentPlace =
            remember { mutableStateOf<Place?>(null) }
        if (place != null) {
            currentPlace.value = place
            currentGlobalPlace = currentPlace.value
            PlaceDetailsContent(context, placesData, currentPlace = currentPlace)
        } else {
            ShowPlaceHolder()
        }
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

    @Composable
    fun TopBar(currentPlace: MutableState<Place?>) {
        TopAppBar(
            elevation = 0.dp,
            backgroundColor = Color.Transparent,
            modifier = Modifier
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(onClick = {
                findNavController().popBackStack()
            }) {

                Icon(
                    Icons.Filled.ArrowBackIos,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            currentPlace.value?.let {
                Text(
                    text = it.place_name,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    fun PlaceDetailsContent(
        context: FragmentActivity,
        placesData: List<Place>,
        currentPlace: MutableState<Place?>
    ) {
        val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
        BackdropScaffold(
            persistentAppBar = false,
            peekHeight = 100.dp,
            stickyFrontLayer = true,
            backLayerBackgroundColor = MaterialTheme.colors.background,
            scaffoldState = scaffoldState,
            appBar = {
                Spacer(modifier = Modifier.statusBarsHeight())
                TopBar(currentPlace)
            },
            frontLayerScrimColor = Color.Unspecified,
            backLayerContent = {
                currentPlace.value?.let {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .padding(bottom = 10.dp)
                    ) {
                        PlaceBanner(
                            place = it, modifier = Modifier.fillMaxSize()
                        )
                        TopBar(currentPlace)
                    }

                }

            },
            frontLayerContent = {
                PlaceSections(
                    context,
                    modifier = Modifier
                        .fillMaxSize(),
                    currentPlace,
                    placesData,
                )
            })

    }

    @ExperimentalAnimationApi
    @Composable
    fun PlaceSections(
        context: FragmentActivity,
        modifier: Modifier = Modifier,
        currentPlace: MutableState<Place?>,
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
                currentPlace.value?.let { innerPlace ->
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                    ) {

                        item {

                            PlacesDescription(
                                innerPlace.place_name,
                                innerPlace.place_description
                            )
                        }

                        if (innerPlace.pano[0].isNotEmpty()) {
                            item {
                                val panoUrl = TrinidadAssets.getAssetFullPath(
                                    innerPlace.pano[0],
                                    TrinidadAssets.webp
                                )
                                PlacePano360(context, Uri.parse(panoUrl))
                            }
                        }

                        if (innerPlace.video_promo.isNotEmpty()) {
                            item {
                                PlacesVideo(innerPlace.video_promo)
                            }
                        }
                        item {
                            OtherPlaces(placesData, currentPlace)
                        }
                        item {
                            Spacer(modifier = Modifier.padding(20.dp))
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun OtherPlaces(placesList: List<Place>, currentPlace: MutableState<Place?>) {
        Card(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                PlaceText(
                    text = stringResource(R.string.others_places)
                )
                LazyRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    items(placesList) { place ->
                        OtherPlaceItem(
                            place
                        ) {
                            currentPlace.value = place
                            Log.d("TAG-CLICKED", place.place_name)
                        }
                    }
                }

            }
        }

    }


    @Composable
    fun OtherPlaceItem(place: Place, setCurrentPlace: () -> Unit) {
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
                    setCurrentPlace()
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                val imagePainter = rememberImagePainter(
                    data = TrinidadAssets.getAssetFullPath(
                        place.header_images[0],
                        TrinidadAssets.jpg
                    ),
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
                    text = place.place_name,
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
                PlaceText(
                    text = "Video",
                )
                VideoPlayer(videoPromo = videoPromo)
            }

        }
    }

    @Composable
    private fun PlacePano360(context: FragmentActivity, imageUrl: Uri) {
        Card(
            elevation = 0.dp,
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                PlaceText(text = stringResource(R.string.imagen360))
                Spacer(Modifier.height(4.dp))
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
    private fun PlacesActionButtons(place: Place) {
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
                //GoToMap
                Button(
                    onClick = {

                        goToMap(placeId = place.place_id)

                    },
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
                //Speaker
                var isSelected by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = {
                        isSelected = if (isSelected) {
                            textToSpeechEngine.stop()
                            false
                        } else {
                            speechPlaceDescription(place.place_description)
                            true
                        }
                    },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    border = BorderStroke(1.dp, colorResource(R.color.trinidadColorPrimary)),
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.trinidadColorPrimary))
                ) {
                    AnimatedContent(
                        targetState = isSelected,
                        transitionSpec = {
                            // Compare the incoming number with the previous number.
                            if (isSelected) {
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
                    ) { targetIsSelected ->
                        Icon(
                            painterResource(
                                if (!targetIsSelected) R.drawable.ic_baseline_hearing_24 else R.drawable.ic_baseline_hearing_disabled_24
                            ),
                            contentDescription = "button description"
                        )
                    }
                }

                //ShareButton
                OutlinedButton(
                    onClick = {
                        sharePlaceInformation()
                    },
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
                //Goto URL
                OutlinedButton(
                    onClick = {
                        goToWebPage(place.web)
                    },
                    modifier = Modifier.size(50.dp),  //avoid the oval shape
                    shape = CircleShape,
                    border = BorderStroke(1.dp, colorResource(R.color.trinidadColorPrimary)),
                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = colorResource(R.color.trinidadColorPrimary))
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
    fun PlaceBanner(place: Place, modifier: Modifier = Modifier) {
        Column(modifier = modifier) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp - 80.dp),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                painter = rememberImagePainter(
                    data = TrinidadAssets.getAssetFullPath(
                        place.header_images[0],
                        TrinidadAssets.jpg
                    ),
                    builder = {
                        crossfade(true)
                        placeholder(R.drawable.placeholder_1)
                    }
                )
            )
            PlacesActionButtons(place = place)
            Spacer(Modifier.height(8.dp))
        }
    }

    @Composable
    fun PlaceText(text: String) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }

    @Composable
    fun VideoPlayer(videoPromo: String) {
        val context = LocalContext.current
        simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        playerView = PlayerView(context).apply {
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

    override fun onStop() {
        releasePlayer()
        textToSpeechEngine.stop()
        super.onStop()

    }

    override fun onResume() {
        textToSpeechEngine.stop()
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeechEngine.stop()
    }

    private fun releasePlayer() {
        if (playerView != null) {
            playWhenReady = simpleExoPlayer.playWhenReady
            playbackPosition = simpleExoPlayer.currentPosition
            currentWindows = simpleExoPlayer.currentWindowIndex
            simpleExoPlayer.release()
        }
    }


    private fun goToWebPage(web: String) {
        TrinidadCustomChromeTab.launch(requireContext(), Uri.parse(web))
    }

    private fun sharePlaceInformation() {
        if (!hasWriteExternalPermission()) {
            requestWriteExternalPermission()
        } else {
            sharePlaceInfo()
        }
    }

//WRITE EXTERNAL STORAGE PERMISSIONS

    private fun hasWriteExternalPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun requestWriteExternalPermission() {
        EasyPermissions.requestPermissions(
            this,
            resources.getString(R.string.rationale_share_message),
            WRITE_EXTERNAL_PERMISSION_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestWriteExternalPermission()
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        if (requestCode == WRITE_EXTERNAL_PERMISSION_CODE) {
            sharePlaceInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object {
        const val WRITE_EXTERNAL_PERMISSION_CODE = 5637
        const val MAX_SMART_TRUNCATE_STRINGS = 256
    }

    private fun sharePlaceInfo() {
        currentGlobalPlace?.place_name?.let { placeName ->
            ShareIntent.loadImageAndShare(
                requireContext(), Uri.parse(
                    currentGlobalPlace?.let { nPlace ->
                        TrinidadAssets.getAssetFullPath(
                            nPlace.header_images[0],
                            TrinidadAssets.jpg
                        )
                    }
                ),
                placeName, resources.getString(R.string.app_name)
            )
        }

    }

    private fun speechPlaceDescription(placeDescription: String) {
        if (placeDescription.isNotEmpty()) {
            val descriptionToSpeech = RomanNumbers.replaceRomanNumber(placeDescription)
            textToSpeechEngine.speak(descriptionToSpeech, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        }
    }

    override fun onPause() {
        textToSpeechEngine.stop()

        releasePlayer()
        super.onPause()
    }

    override fun onDestroy() {
        textToSpeechEngine.stop()

        super.onDestroy()
    }

}




