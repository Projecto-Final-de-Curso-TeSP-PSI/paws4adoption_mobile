package amsi.dei.estg.ipleiria.paws4adoption.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.paws4adoption.R;
import amsi.dei.estg.ipleiria.paws4adoption.models.Organization;

public class OrganizationListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Organization> organizations;

    public OrganizationListAdapter(Context context, ArrayList<Organization> organizations){
        this.context = context;
        this.organizations = organizations;
    }

    @Override
    public int getCount() {
        return organizations.size();
    }

    @Override
    public Object getItem(int i) {
        return organizations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        if(layoutInflater == null){
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.fragment_item_list_organization, null);
        }

        ListViewHolder listViewHolder = (ListViewHolder) convertView.getTag();
        if(listViewHolder == null){
            listViewHolder = new ListViewHolder(convertView);
            convertView.setTag(listViewHolder);
        }

        listViewHolder.update(organizations.get(i));
        return convertView;
    }

    private class ListViewHolder {

        private TextView name, address, phone, email;

        public ListViewHolder(View convertView) {
            name = convertView.findViewById(R.id.tvName);
            address = convertView.findViewById(R.id.tvAddress);
            phone = convertView.findViewById(R.id.tvPhone);
            email = convertView.findViewById(R.id.tvEmail);
        }

        public void update(Organization organization){
            name.setText(organization.getName());
            address.setText(organization.getStreet() + " " + organization.getDoor_number() + ", " + organization.getFloor() + " - " + organization.getPostal_code() + " - " + organization.getStreet_code() + " " + organization.getCity() );
            phone.setText(organization.getPhone());
            email.setText(organization.getEmail());
        }

    }

}
