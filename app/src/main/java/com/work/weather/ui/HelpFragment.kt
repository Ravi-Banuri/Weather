package com.work.weather.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.work.weather.R
import kotlinx.android.synthetic.main.fragment_help.*


class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var helpText = getText(R.string.help_text).toString()
        //help_web_view.loadData(helpText, "text/html", null)
        help_web_view.loadUrl("file:///android_asset/help.html");
    }

}