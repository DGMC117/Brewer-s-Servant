package magicchief.main.brewersservant.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChart.polar
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.*
import com.anychart.scales.Linear
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R


class DeckStatsFragment : Fragment() {

    var deckId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deckId = it.getInt("deckId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dbHelper = DBHelper(requireContext())

        val cardsInDeck = dbHelper.getDeckCards(deckId)

        var planeswalkerCount = 0
        var creatureCount = 0
        var artifactCount = 0
        var enchantmentCount = 0
        var sorceryCount = 0
        var instantCount = 0
        var landCount = 0

        var manaValueArray = arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

        var symbolStarted = false
        var whiteSymbolsInCost = 0
        var blueSymbolsInCost = 0
        var blackSymbolsInCost = 0
        var redSymbolsInCost = 0
        var greenSymbolsInCost = 0
        var colorlessSymbolsInCost = 0

        var whiteManaProducers = 0
        var blueManaProducers = 0
        var blackManaProducers = 0
        var redManaProducers = 0
        var greenManaProducers = 0
        var colorlessManaProducers = 0

        cardsInDeck.forEach {
            if (it.cmc!!.toInt() > 7) manaValueArray[8]++
            else if (!it.type_line!!.contains("Land")) manaValueArray[it.cmc!!.toInt()]++
            if (it.type_line!!.contains("Planeswalker")) planeswalkerCount++
            if (it.type_line!!.contains("Creature")) creatureCount++
            if (it.type_line!!.contains("Artifact")) artifactCount++
            if (it.type_line!!.contains("Enchantment")) enchantmentCount++
            if (it.type_line!!.contains("Sorcery")) sorceryCount++
            if (it.type_line!!.contains("Instant")) instantCount++
            if (it.type_line!!.contains("Land")) landCount++

            it.produced_mana?.forEach {
                when (it) {
                    "W" -> whiteManaProducers++
                    "U" -> blueManaProducers++
                    "B" -> blackManaProducers++
                    "R" -> redManaProducers++
                    "G" -> greenManaProducers++
                    "C" -> colorlessManaProducers++
                }
            }

            it.mana_cost?.forEach {
                when (it) {
                    '{' -> symbolStarted = true
                    '}' -> symbolStarted = false
                    'W' -> if (symbolStarted) whiteSymbolsInCost++
                    'U' -> if (symbolStarted) blueSymbolsInCost++
                    'B' -> if (symbolStarted) blackSymbolsInCost++
                    'R' -> if (symbolStarted) redSymbolsInCost++
                    'G' -> if (symbolStarted) greenSymbolsInCost++
                    'C' -> if (symbolStarted) colorlessSymbolsInCost++
                }
            }
        }

        val manaValueChartView = requireView().findViewById<AnyChartView>(R.id.mana_curve_chart_view)
        APIlib.getInstance().setActiveAnyChartView(manaValueChartView)
        val manaValueCartesian = AnyChart.column()
        val manaValueData: MutableList<DataEntry> = ArrayList()
        manaValueData.add(ValueDataEntry("0", manaValueArray[0]))
        manaValueData.add(ValueDataEntry("1", manaValueArray[1]))
        manaValueData.add(ValueDataEntry("2", manaValueArray[2]))
        manaValueData.add(ValueDataEntry("3", manaValueArray[3]))
        manaValueData.add(ValueDataEntry("4", manaValueArray[4]))
        manaValueData.add(ValueDataEntry("5", manaValueArray[5]))
        manaValueData.add(ValueDataEntry("6", manaValueArray[6]))
        manaValueData.add(ValueDataEntry("7", manaValueArray[7]))
        manaValueData.add(ValueDataEntry("8+", manaValueArray[8]))
        val manaValueCartesianColumn = manaValueCartesian.column(manaValueData)
        manaValueCartesianColumn.tooltip()
            .titleFormat(getString(R.string.mana_value) + " {%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0)
            .offsetY(5)
            .format(getString(R.string.cards) + ": {%Value}")
        manaValueCartesian.animation(true)
        manaValueCartesian.title(getString(R.string.mana_value_curve))
        manaValueCartesian.yAxis(0).labels().format("{%Value}")
        manaValueCartesian.yScale().minimum(0)
        manaValueCartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        manaValueCartesian.interactivity().hoverMode(HoverMode.SINGLE)
        manaValueCartesian.xAxis(0).title(getString(R.string.mana_value_label))
        manaValueCartesian.yAxis(0).title(getString(R.string.cards))
        manaValueCartesian.background().fill(requireActivity().getColor(R.color.colorBackground).toString())
        manaValueChartView.setChart(manaValueCartesian)

        val typesChartView = requireView().findViewById<AnyChartView>(R.id.types_chart_view)
        APIlib.getInstance().setActiveAnyChartView(typesChartView)
        val typesPie = AnyChart.pie()
        val typesChartData: MutableList<DataEntry> = ArrayList()
        typesChartData.add(ValueDataEntry("Planeswalker", planeswalkerCount))
        typesChartData.add(ValueDataEntry("Creature", creatureCount))
        typesChartData.add(ValueDataEntry("Artifact", artifactCount))
        typesChartData.add(ValueDataEntry("Enchantment", enchantmentCount))
        typesChartData.add(ValueDataEntry("Sorcery", sorceryCount))
        typesChartData.add(ValueDataEntry("Instant", instantCount))
        typesChartData.add(ValueDataEntry("Land", landCount))
        typesPie.data(typesChartData)
        typesPie.title(getString(R.string.card_types_distribution))
        typesPie.labels().position("outside")
        typesPie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE).align(Align.CENTER)
        typesPie.background().fill(requireActivity().getColor(R.color.colorBackground).toString())
        typesChartView.setChart(typesPie)

        val symbolsChartView = requireView().findViewById<AnyChartView>(R.id.mana_cost_color_symbols_chart_view)
        APIlib.getInstance().setActiveAnyChartView(symbolsChartView)
        val symbolsPolar = AnyChart.polar()
        val symbolsChartData: MutableList<DataEntry> = ArrayList()
        symbolsChartData.add(CustomDataEntry(getString(R.string.white), whiteSymbolsInCost, whiteManaProducers))
        symbolsChartData.add(CustomDataEntry(getString(R.string.blue), blueSymbolsInCost, blueManaProducers))
        symbolsChartData.add(CustomDataEntry(getString(R.string.black), blackSymbolsInCost, blackManaProducers))
        symbolsChartData.add(CustomDataEntry(getString(R.string.red), redSymbolsInCost, redManaProducers))
        symbolsChartData.add(CustomDataEntry(getString(R.string.green), greenSymbolsInCost, greenManaProducers))
        symbolsChartData.add(CustomDataEntry(getString(R.string.colorless), colorlessSymbolsInCost, colorlessManaProducers))
        val symbolsSet = Set.instantiate()
        symbolsSet.data(symbolsChartData)
        val symbolsData = symbolsSet.mapAs("{ x: 'x', value: 'value' }")
        val producedData = symbolsSet.mapAs("{ x: 'x', value: 'value2' }")
        val columnSymbols = symbolsPolar.column(symbolsData)
        val columnProduced = symbolsPolar.column(producedData)
        columnSymbols.tooltip().format(getString(R.string.mana_symbols) + ": {%value}")
        columnSymbols.name(getString(R.string.mana_symbols))
        columnProduced.tooltip().format(getString(R.string.produced_mana) + ": {%value2}")
        columnProduced.name(getString(R.string.produced_mana))
        symbolsPolar.title(getString(R.string.symbols_vs_produced))
        symbolsPolar.sortPointsByX(true)
            .defaultSeriesType(PolarSeriesType.COLUMN)
            .yAxis(false)
            .xScale(ScaleTypes.ORDINAL)
        symbolsPolar.title().margin().bottom(20)
        (symbolsPolar.yScale(Linear::class.java) as Linear).stackMode(ScaleStackMode.PERCENT)
        symbolsPolar.tooltip()
            .displayMode(TooltipDisplayMode.UNION)
        symbolsPolar.background().fill(requireActivity().getColor(R.color.colorBackground).toString())
        symbolsPolar.legend(true)
        symbolsChartView.setChart(symbolsPolar)

    }

    private class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
        }
    }
}