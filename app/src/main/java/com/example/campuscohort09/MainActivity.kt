package com.example.campuscohort09

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Currency

data class CampusResource(
    @param:StringRes val namesRes: Int,
    @param:StringRes val category: Int,
    @param:StringRes val hoursRes: Int,
    val lastUpdate: LocalDate,
    val reviewCount: Int,
    val capacity: Int,
    val fee: Double
)


val sampleResource = listOf(
    CampusResource(
        namesRes = R.string.resource_intl_office_name,
        category = R.string.resource_intl_office_category,
        hoursRes = R.string.resource_intl_office_hours,
        lastUpdate = LocalDate.of(2026,8,5),
        reviewCount = 3,
        capacity = 40,
        fee = 2.99
    ),
    CampusResource(
        namesRes = R.string.resource_library_name,
        category = R.string.resource_library_category,
        hoursRes = R.string.resource_library_hours,
        lastUpdate = LocalDate.of(2026,6,12),
        reviewCount = 13,
        capacity = 20,
        fee = 5.99
    ),
    CampusResource(
        namesRes = R.string.resource_veterans_name,
        category = R.string.resource_veterans_category,
        hoursRes = R.string.resource_veterans_hours,
        lastUpdate = LocalDate.of(2026,2,1),
        reviewCount = 1,
        capacity = 10,
        fee = 0.99
    )
)
data class CampusTip(
    @param:StringRes val titleRes: Int,
    @param:StringRes val descriptionRes: Int
)
val sampleTips = listOf(
    CampusTip(
        titleRes = R.string.tip_documents_title,
        descriptionRes = R.string.tip_documents_description
    ),
    CampusTip(
        titleRes = R.string.tip_study_rooms_title,
        descriptionRes = R.string.tip_study_rooms_description
    ),
    CampusTip(
        titleRes = R.string.tip_clubs_title,
        descriptionRes = R.string.tip_clubs_description
    )
)

class MainActivity: ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme{
                CampusConnectScreen()
            }
        }
    }
}
@Composable
fun CampusConnectScreen(){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(stringResource(R.string.app_title), style = MaterialTheme.typography.headlineMedium)
                Text(stringResource(id = R.string.app_subtitle), style = MaterialTheme.typography.bodyMedium)
            }
        }

        item {
            SectionHeader(titleRes = R.string.resources_section_header)
        }

        items(sampleResource){ resource ->
            ResourceCard(resource)
        }

        item {
            SectionHeader(
                titleRes = R.string.tips_section_header,
                descriptionRes = R.string.tips_section_description
            )
        }

        items(sampleTips){ tip ->
            TipCard(tip)
        }
    }
}


@Composable
fun SectionHeader(@StringRes titleRes: Int, @StringRes descriptionRes: Int? = null){
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text(
            stringResource(titleRes),
            style = MaterialTheme.typography.titleSmall
        )

        if (descriptionRes != null){
            Text(
                stringResource(descriptionRes),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Composable
fun ResourceCard(resource: CampusResource){

    val context = LocalContext.current
    val locale = LocalConfiguration.current.locales[0]

    val dateFullFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale)
    val dateShortFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale)

    val currencyFormatter = NumberFormat.getCurrencyInstance(locale).apply {
        currency = Currency.getInstance("USD")
    }
    val integerFormatter = NumberFormat.getIntegerInstance(locale)


    val name = stringResource(resource.namesRes)
    val category = stringResource(resource.category)

    val hours = stringResource(
        R.string.hours_label,
        stringResource(resource.hoursRes)
    )

    val reviewText = context.resources.getQuantityString(
        R.plurals.review_count,
        resource.reviewCount,
        resource.reviewCount
    )

    val capacityFormatted = integerFormatter.format(resource.capacity)


    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)){

            Text(
                name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                category,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                hours
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(stringResource(R.string.capacity_label, capacityFormatted))

            Text(reviewText)

            if (resource.fee > 0.0){
                Text(
                    stringResource(R.string.fee_label,
                        currencyFormatter.format(resource.fee))
                )
            }

            Text(stringResource(R.string.updated_short_label, resource.lastUpdate.format(dateShortFormatter)))

            Text(stringResource(R.string.last_updated_label, resource.lastUpdate.format(dateFullFormatter)))
        }
    }

}

@Composable
fun TipCard(tip: CampusTip){
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                stringResource(tip.titleRes),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                stringResource(tip.descriptionRes),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Preview(name = "English", showBackground = true, locale = "en", heightDp = 1400)
@Preview(name = "Español", showBackground = true, locale = "es", heightDp = 1400)
@Preview(name = "Français", showBackground = true, locale = "fr", heightDp = 1400)
@Composable
fun CampusConnectScreenPreview(){
    MaterialTheme {
        CampusConnectScreen()
    }
}