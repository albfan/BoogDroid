package jp.bugscontrol.bugzilla;

import org.json.JSONArray;
import org.json.JSONObject;

public class Server extends jp.bugscontrol.server.Server {
    public interface Listener {
        void callback(String s);
    }

    public Server() {}

    @Override
    protected void loadProducts() {
        // Get all the products' ids and pass it to loadProductsFromIds()
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    loadProductsFromIds(object.getJSONObject("result").getString("ids"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask("Product.get_accessible_products", l);
        task.execute();
    }

    @Override
    protected void loadBugsForProduct(final jp.bugscontrol.server.Product p) {
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray bugs = object.getJSONObject("result").getJSONArray("bugs");
                    for (int i=0; i < bugs.length(); ++i) {
                        p.addBug(new Bug(bugs.getJSONObject(i)));
                    }
                    bugsListUpdated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask("Bug.search", "\"product\":\"" + p.getName() + "\"", l);
        task.execute();
    }

    void loadProductsFromIds(String product_ids) {
        Listener l = new Listener() {
            @Override
            public void callback(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray products_json = object.getJSONObject("result").getJSONArray("products");
                    for (int i=0; i<products_json.length(); ++i) {
                        JSONObject p = products_json.getJSONObject(i);
                        products.add(new Product(p));
                    }
                    productsListUpdated();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        BugzillaTask task = new BugzillaTask("Product.get", "\"ids\":" + product_ids, l);
        task.execute();
    }
}
