package com.inmersoft.trinidadpatrimonial.ui.trinidad.details.place

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.navArgs
import coil.compose.rememberImagePainter
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.composables.ComposePanoView
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.placeholderList
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class PlaceContainerDetailsFragment : Fragment() {

    private val safeArgs: PlaceContainerDetailsFragmentArgs by navArgs()
    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(requireContext()).apply {
        setContent {
            PlaceDetailScreen(requireActivity(), trinidadDataViewModel.allPlaces)
        }

    }


    @ExperimentalMaterialApi
    @Composable
    fun PlaceDetailScreen(context: FragmentActivity, placesLiveData: LiveData<List<Place>>) {
        val placesData by placesLiveData.observeAsState(initial = emptyList())
        if (placesData.isEmpty()) {
            ShowLoading()
        } else {
            PlaceDetailsContent(context, placesData)
        }
    }

    @Composable
    fun ShowLoading() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .width(120.dp)
                    .height(1.dp)
                    .alpha(0.4f),
                color = colorResource(R.color.trinidadColorOnPrimary),
                backgroundColor = colorResource(R.color.background_progressbar_color)
            )
        }
    }


    @ExperimentalMaterialApi
    @Composable
    fun PlaceDetailsContent(context: FragmentActivity, placesData: List<Place>) {

        val userSelectPlaceId = safeArgs.placeID
        val currentPlace = placesData.first() { place -> place.place_id == userSelectPlaceId }

        Column(modifier = Modifier.fillMaxSize()) {
            BackdropScaffold(
                backLayerBackgroundColor = Color.White,
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
    }


    @Composable
    fun PlaceSections(
        context: FragmentActivity,
        modifier: Modifier = Modifier,
        currentPlace: Place,
        placesData: List<Place>,
    ) {
        Surface(
            modifier = modifier.fillMaxSize(), color = Color.White, shape = RoundedCornerShape(
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
                        PlacesDescription(currentPlace.place_name, currentPlace.place_description)
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
        Card(modifier = Modifier.fillMaxSize(), elevation = 2.dp) {
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
            modifier = Modifier
                .height(130.dp)
                .width(180.dp)
                .padding(start = 4.dp, top = 8.dp, bottom = 8.dp, end = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    contentScale = ContentScale.Crop,
                    contentDescription = "OtherPlaces",
                    painter = rememberImagePainter(
                        data = imageUrl,
                        builder = {
                            crossfade(true)
                            placeholder(placeholderList[Random.nextInt(placeholderList.size)])
                        }
                    )
                )
                Text(
                    text = placeName,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }


    }


    @Composable
    private fun PlacesVideo(videoPromo: String) {
        Card(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Places Video", textAlign = TextAlign.Center)
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
                    text = stringResource(R.string.imagen360),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                ComposePanoView(
                    context,
                    panoUri = imageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    @Composable
    private fun PlacesDescription(placeName: String, placeDescription: String) {
        Card(
            modifier = Modifier
                .height(210.dp)
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp, bottom = 8.dp)
        ) {
            val verticalScrollDescriptionState = rememberScrollState(0)
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = placeName, textAlign = TextAlign.Left)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = placeDescription,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.verticalScroll(verticalScrollDescriptionState)
                )
            }
        }
    }


    @Composable
    private fun PlacesActionButtons() {
        Card(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            elevation = 4.dp,
            shape = RoundedCornerShape(3.dp)
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
                        placeholder(com.inmersoft.trinidadpatrimonial.R.drawable.placeholder_1)
                    }
                )
            )
            PlacesActionButtons()
            Spacer(Modifier.height(8.dp))
        }
    }


    private fun subscribeObservers() {
        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, { allPlaces ->
            val fragmentList = mutableListOf<PlaceDetailsFragment>()
            allPlaces.indices.forEach { index ->
                val currentPlace = allPlaces[index]
                if (safeArgs.placeID == currentPlace.place_id) {
                    //Agregamos el lugar elejido por el usuario como primero en la lista
                    fragmentList.add(0, PlaceDetailsFragment(currentPlace))
                } else {
                    fragmentList.add(PlaceDetailsFragment(currentPlace))
                }
            }
        })
    }


/*

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    private fun ExploreItem(
        modifier: Modifier = Modifier,
        item: ExploreModel,
        onItemClicked: OnExploreItemClicked,
    ) {
        Row(
            modifier = modifier
                .clickable { onItemClicked(item) }
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            ExploreImageContainer {
                Box {
                    val painter = rememberImagePainter(
                        data = item.imageUrl,
                        builder = {
                            crossfade(true)
                        }
                    )
                    Image(
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )

                    if (painter.state is Loading) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_crane_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.Center),
                        )
                    }
                }
            }
            Spacer(Modifier.width(24.dp))
            Column {
                Text(
                    text = item.city.nameToDisplay,
                    style = MaterialTheme.typography.h6
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.caption.copy(color = crane_caption)
                )
            }
        }
    }

    @Composable
    private fun ExploreImageContainer(content: @Composable () -> Unit) {
        Surface(Modifier.size(width = 60.dp, height = 60.dp), RoundedCornerShape(4.dp)) {
            content()
        }
    }
*/


}


