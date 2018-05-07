package com.rego.jorge.reusablerecyclersample.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.rego.jorge.reusablerecyclersample.R
import com.rego.jorge.reusablerecycler.extensions.invisible
import com.rego.jorge.reusablerecycler.extensions.visible
import com.rego.jorge.reusablerecycler.main.ReusableRecycler
import kotlinx.android.synthetic.main.activity_sample.*
import kotlinx.android.synthetic.main.item_sample_row2.view.*

/**
 * Created by jorge.rego.casas on 13/04/2018.
 */
class SampleActivity : AppCompatActivity() {

    private lateinit var reusableRecycler: ReusableRecycler<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        frameLayout?.let {
            reusableRecycler = ReusableRecycler(
                    this,
                    it,
                    R.layout.item_sample_row2,
                    ::onBind,
                    ::onClick,
                    null,
                    ::onEdit,
                    ::onRemove
            ).also {
                it.setBackgroundColor(ContextCompat.getColor(this, R.color.blue))
                it.setElements(listOf(
                        Item("name1", "desc1"),
                        Item("name2", "desc2"),
                        Item("name3", "desc3"),
                        Item("name4", "desc4"),
                        Item("name5", "desc5"),
                        Item("name6", "desc6"),
                        Item("name7", "desc7"),
                        Item("name8", "desc8"),
                        Item("name9", "desc9"),
                        Item("name10", "desc10"),
                        Item("name11", "desc11"),
                        Item("name12", "desc12"),
                        Item("name13", "desc13"),
                        Item("name14", "desc14"),
                        Item("name15", "desc15"),
                        Item("name16", "desc16"),
                        Item("name17", "desc17"),
                        Item("name18", "desc18"),
                        Item("name19", "desc19"),
                        Item("name20", "desc20")
                ))
            }
        }
    }

    private fun onAdd() {
        Toast.makeText(this, "On Add item", Toast.LENGTH_SHORT).show()
    }

    private fun onRemove(item: Item, position: Int) {
        Toast.makeText(this, "Element removed ${item.name}", Toast.LENGTH_SHORT).show()
        reusableRecycler.removeElementAtPosition(position)
    }

    private fun onEdit(item: Item, position: Int) {
        Toast.makeText(this, "Element edited ${item.name}", Toast.LENGTH_SHORT).show()
    }

    private fun onClick(item: Item, view: View) {
        Toast.makeText(this, "Element clicked ${item.name}", Toast.LENGTH_SHORT).show()
    }

    private fun onBind(itemView: View, item: Item, position: Int) {
        itemView.checkIcon?.let {
            if (reusableRecycler.getSelectedElementPosition() == position) {
                it.visible()
            } else {
                it.invisible()
            }
        }

        itemView.operationTypeTitle?.text = item.name
    }
}

data class Item(val name: String, val description: String)