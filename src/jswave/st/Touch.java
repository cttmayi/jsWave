/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jswave.st;

/**
 *
 * @author cttmayi
 */
class Touch {
    int x1, x2, y1, y2;
    int id;

    Touch(int id, int x1, int y1, int x2, int y2) {
        this.id = id;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public static Touch newData(int id, int x1, int y1, int x2, int y2) {
        return new Touch(id, x1, y1, x2, y2);
    }

    public int getId() {
        return id;
    }

    public boolean isInRange(int x, int y) {
        if (x1 <= x && x <= x2 && y1 <= y && y <= y2) {
            return true;
        }
        return false;
    }

}
