package kien.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import kien.license.R;
import kien.objects.License;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KIEN on 07/05/2015.
 */
public class AdapterLicense extends ArrayAdapter<License> {
    private LayoutInflater inflater;
    private Context context;
    private List<License> list, listLicense;
    private int resId;
    private OnClickNumber listener;
    private char[] charA = {'à', 'á', 'ạ', 'ả', 'ã', 'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ', 'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ'}; //0 -> 16
    private char[] charE = {'è', 'é', 'ẻ', 'ẽ', 'ẹ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ'}; //17->27
    private char[] charI = {'ì', 'í', 'ỉ', 'ĩ', 'ị'}; //28->32
    private char[] charO = {'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở', 'ỡ', 'ợ'}; //33->49
    private char[] charU = {'ù', 'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự'}; //50->60
    private char[] charY = {'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ'};//61->65
    private char[] charD = {'đ', ' '}; //66->67
    String charact = String.valueOf(charA, 0, charA.length) + String.valueOf(charE, 0, charE.length) + String.valueOf(charI, 0, charI.length) +
            String.valueOf(charO, 0, charO.length) + String.valueOf(charU, 0, charU.length)+ String.valueOf(charY, 0, charY.length)
            + String.valueOf(charD, 0, charD.length);
    private char getAlterChar(char pC){
        if ((int) pC == 32 ){
            return ' ';
        }
        char temp = pC;
        int i = 0;
        while (i < charact.length() && charact.charAt(i) != temp){
            i++;
        }
        if (i < 0 || i > 67) {
            return pC;
        }
        if (i == 66) {
            return 'd';
        }
        if (i >=0 && i <= 16){
            return 'a';
        }
        if (i >=17 && i <= 27){
            return 'e';
        }
        if (i>=28 && i<=32){
            return 'i';
        }
        if (i>=33 && i<=49){
            return 'o';
        }
        if (i>=50 && i<=60){
            return 'u';
        }
        if (i>=61 && i<=65){
            return 'y';
        }
        return temp;
    }
    public String convertToVietNam(String srtDes){
        String convertString = srtDes.toLowerCase();
        for (int i =0; i<convertString.length(); i++) {
            char temp = convertString.charAt(i);
            if ((int) temp <97 || temp > 122){
                char temp1 = this.getAlterChar(temp);
                if ((int) temp1 != 32) {
                    convertString = convertString.replace(temp, temp1);
                }
            }
        }
        return  convertString;
    }
    public interface OnClickNumber {
        public void changeToPage(int position);
    }

    public AdapterLicense(Context context, int resId, List<License> list, OnClickNumber listener) {
        super(context, resId);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        this.resId = resId;
        this.listener = listener;
        this.listLicense = list;


    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final License lc = list.get(position);
        String[] color = {"#e3e3e3", "#4d6df8", "#7cf7f0", "#f0b753", "#58953d", "#dd3939", "#e9a881", "#441155"};
        String[] textColor = {"#949391", "#ffffff", "#667ee5", "#50eb54", "#e2e74c", "#ffffff", "#f9fc14", "#ffffff"};
        int numOfColor = 1;
        Holder holder = new Holder();
        if (convertView == null) {
            convertView = inflater.inflate(resId, parent, false);

            holder.tvNumberProvince = (TextView) convertView.findViewById(R.id.tvNumber);
            holder.tvProvince = (TextView) convertView.findViewById(R.id.tvProvince);
            holder.llProvince = (LinearLayout) convertView.findViewById(R.id.llProvince);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.llProvince.setBackgroundColor(Color.parseColor(color[position % numOfColor]));
        holder.tvNumberProvince.setTextColor(Color.parseColor(textColor[position % numOfColor]));
        holder.tvProvince.setTextColor(Color.parseColor(textColor[position % numOfColor]));
        holder.tvNumberProvince.setText(lc.getNumberProvince());
        holder.tvProvince.setText(lc.getProvince());
        char firstName = lc.getNumberProvince().charAt(0);
        if (firstName <= 'Z' && firstName >= 'A') {
            holder.llProvince.setBackgroundColor(Color.parseColor("#f54051"));
            holder.tvNumberProvince.setTextColor(Color.parseColor("#cccecb"));
            holder.tvProvince.setTextColor(Color.parseColor("#cccecb"));
        }
        if (lc.getNumberProvince().equals("80")) {
            holder.llProvince.setBackgroundColor(Color.parseColor("#4a5af7"));
            holder.tvNumberProvince.setTextColor(Color.parseColor("#cccecb"));
            holder.tvProvince.setTextColor(Color.parseColor("#cccecb"));
        }
        holder.llProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = lc.getId();
                String numberProvince = lc.getNumberProvince();
                try {
                    int firstNumber = Integer.parseInt(numberProvince.charAt(0) + "");
                    Log.e("IDNAME", firstNumber + "");
                    if (!numberProvince.equals("80") && (0 <= firstNumber && firstNumber <= 99)) {
                        listener.changeToPage(id);
                    }
                } catch (Exception e) {

                }


            }
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase();
                constraint = convertToVietNam(constraint.toString());
                FilterResults result = new FilterResults();
                Log.e("FILTER", constraint.toString());
                if (constraint != null && constraint.length() > 0) {
                    ArrayList<License> filteredItems = new ArrayList<License>();
                    for (int i = 0; i < listLicense.size(); i++) {
                        License license = listLicense.get(i);
                        //Log.e("GETFILTER", license.getProvince() +"");

                        if (convertToVietNam(license.getNumberProvince().toString().toLowerCase()).contains(constraint) || convertToVietNam(license.getProvince().toString().toLowerCase()).contains(constraint)) {
                            filteredItems.add(license);
                        }
                        result.count = filteredItems.size();
                        result.values = filteredItems;
                    }
                } else {
                    synchronized (this) {
                        result.count = listLicense.size();
                        result.values = listLicense;
                    }

                }
                //Log.e("GETFILTER", result.count +"");
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<License>) results.values;
                notifyDataSetChanged();
                clear();
                for (int i = 0; i < list.size(); i++) {
                    add(list.get(0));
                }
                notifyDataSetInvalidated();
            }
        };
    }

    static class Holder {
        TextView tvNumberProvince, tvProvince;
        LinearLayout llProvince;
    }
}
