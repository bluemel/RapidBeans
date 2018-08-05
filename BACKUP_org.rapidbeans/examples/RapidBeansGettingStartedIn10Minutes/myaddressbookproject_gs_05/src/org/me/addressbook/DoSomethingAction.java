package org.me.addressbook;

import java.util.List;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.datasource.Document;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.service.Action;

public class DoSomethingAction extends Action {
    @SuppressWarnings("unchecked")
    @Override
    public void execute() {
        final Application app = ApplicationManager.getApplication();
        final Document doc = app.getActiveDocument();
        final StringBuffer sb = new StringBuffer();
        for (final RapidBean bean : doc.findBeansByQuery("org.me.addressbook.Person")) {
            sb.append("Person: ");
            sb.append(bean.getPropValue("lastname"));
            sb.append(", ");
            sb.append(bean.getPropValue("firstname"));
            sb.append(", groups: ");
            final List<RapidBean> groups = (List<RapidBean>) bean.getPropValue("groups");
            for (int i = 0; i < groups.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(groups.get(i).getPropValue("name"));
            }
            sb.append('\n');
        }
        app.messageInfo(sb.toString(), "Address List Report");
    }
}
