package com.inri.sopsop.view.results;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.inri.sopsop.R;
import com.inri.sopsop.Utils;
import com.inri.sopsop.view.base.SimpleFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Railag on 20.03.2017.
 */

public class ReactionResultsFragment extends SimpleFragment implements ResultScreen {

    public final static String RESULTS = "results";

    public static ReactionResultsFragment newInstance(Bundle args) {

        ReactionResultsFragment fragment = new ReactionResultsFragment();
        fragment.setHasOptionsMenu(true);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.averageReact)
    TextView averageReact;

    @BindView(R.id.minReact)
    TextView minReact;

    @BindView(R.id.maxReact)
    TextView maxReact;

    @BindView(R.id.chart)
    LineChart chart;

    @Override
    protected String getTitle() {
        return getString(R.string.resultsTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.results, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
            default:
                save();
        }

        return super.onOptionsItemSelected(item);
    }

    private void save() {
        PrintAttributes attributes = new PrintAttributes.Builder()
                .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                .setMediaSize(PrintAttributes.MediaSize.ISO_A2.asPortrait())
                .setResolution(new PrintAttributes.Resolution("Reaction results", "Reaction results", 300, 300))
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                .build();

        PrintedPdfDocument document = new PrintedPdfDocument(getActivity(),
                attributes);

        PdfDocument.Page page = document.startPage(0);

        View content = getView();
        content.draw(page.getCanvas());

        document.finishPage(page);

        if (!canWriteOnExternalStorage() || !Utils.canWrite(getActivity())) {
            // TODO
            return;
        }

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/psychology");
        dir.mkdir();

        File newFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/psychology/reaction_results.pdf");

        String name = newFile.getAbsolutePath();

        try {
            OutputStream stream =
                    new FileOutputStream(newFile, true);
            document.writeTo(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        Toast.makeText(getActivity(), "Results saved to " + name, Toast.LENGTH_SHORT).show();
    }

    public static boolean canWriteOnExternalStorage() {
        // get the state of your external storage
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // if storage is mounted return true
            return true;
        }
        return false;
    }

    @Override
    protected int getViewId() {
        return R.layout.results_reaction_layout;
    }

    @Override
    protected void initView(View v) {
        Bundle args = getArguments();

        Utils.verifyStoragePermissions(getActivity());

        if (args != null) {
            if (args.containsKey(RESULTS)) {
                ArrayList<Double> results = (ArrayList<Double>) args.getSerializable(RESULTS);


                double average = 0;
                for (int i = 0; i < results.size(); i++) {
                    average += results.get(i);
                }

                average /= results.size();

                averageReact.setText(String.valueOf(average));

                double min = Collections.min(results);
                double max = Collections.max(results);

                minReact.setText(String.valueOf(min));
                maxReact.setText(String.valueOf(max));

                List<Entry> lineEntries = new ArrayList<>();

                for (int i = 0; i < results.size(); i++) {
                    lineEntries.add(new Entry(i, results.get(i).floatValue()));
                }

                LineDataSet dataSet = new LineDataSet(lineEntries, "Время");
                //dataSet.setColor(...);
                //dataSet.setValueTextColor(...);

                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate();

            }

        }
    }
}
