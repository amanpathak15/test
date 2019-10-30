package com.mindwarriorhack.app.view.Purchase;

import com.mindwarriorhack.app.model.Packages.PackageListItem;

import java.util.List;

public interface PurchaseView
{
    void onSuccess(List<PackageListItem> list);
    void onFailure(String error);

}
