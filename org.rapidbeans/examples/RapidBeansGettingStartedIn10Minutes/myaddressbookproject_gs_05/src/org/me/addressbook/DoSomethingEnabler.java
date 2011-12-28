package org.me.addressbook;

import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.enabler.Enabler;

public class DoSomethingEnabler extends Enabler {

    @Override
    public boolean getEnabled() {
        final Application app = ApplicationManager.getApplication();
        if (app.getActiveDocument() == null
                || app.getActiveDocument().getRoot().getType()
                    != TypeRapidBean.forName("org.me.addressbook.Addressbook")) {
            return false;
        }
        return true;
//        return User.hasRoleGeneric(app.getAuthenticatedUser(), "superadministrator");
    }
}
