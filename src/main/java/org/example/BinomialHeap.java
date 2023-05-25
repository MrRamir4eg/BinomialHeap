package org.example;

import java.util.Objects;

public class BinomialHeap {
    public static class Node {
        int key;
        Node parent;
        Node child;
        Node sibling;
        int degree;

        public Node(int key) {
            this.key = key;
            parent = null;
            child = null;
            sibling = null;
            degree = 0;
        }

        public Node(Node node) {
            this.key = node.key;
            this.parent = node.parent;
            this.child = node.child;
            this.sibling = node.sibling;
            this.degree = node.degree;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (key != node.key) return false;
            if (degree != node.degree) return false;
            if (!Objects.equals(parent, node.parent)) return false;
            if (!Objects.equals(child, node.child)) return false;
            return Objects.equals(sibling, node.sibling);
        }

    }

    Node head;

    public BinomialHeap() {
        head = null;
    }

    public BinomialHeap(int nodeKey) {
        head = new Node(nodeKey);
    }

    public BinomialHeap(Node head) {
        this.head = head;
    }

    public static BinomialHeap merge(BinomialHeap H1, BinomialHeap H2) {
        if (H1 == null || H1.head == null) {
            return H2;
        }
        if (H2 == null || H2.head == null) {
            return H1;
        }

        BinomialHeap H = new BinomialHeap();
        Node curH1 = H1.head;
        Node curH2 = H2.head;
        if (curH1.degree < curH2.degree) {
            H.head = curH1;
            curH1 = curH1.sibling;
        } else if (curH1.degree > curH2.degree) {
            H.head = curH2;
            curH2 = curH2.sibling;
        } else {
            if (curH1.key < curH2.key) {
                H.head = curH1;
                curH1 = curH1.sibling;
            } else {
                H.head = curH2;
                curH2 = curH2.sibling;
            }
        }
        Node curH = H.head;

        while (curH1 != null && curH2 != null) {
            if (curH1.degree < curH2.degree) {
                curH.sibling = curH1;
                curH1 = curH1.sibling;
                curH = curH.sibling;
            } else if (curH1.degree > curH2.degree) {
                curH.sibling = curH2;
                curH2 = curH2.sibling;
                curH = curH.sibling;
            } else {
                if (curH1.key < curH2.key) {
                    curH.sibling = curH1;
                    curH1 = curH1.sibling;
                    curH = curH.sibling;
                } else {
                    curH.sibling = curH2;
                    curH2 = curH2.sibling;
                    curH = curH.sibling;
                }
            }
        }

        if (curH1 == null) {
            while (curH2 != null) {
                curH.sibling = curH2;
                curH2 = curH2.sibling;
                curH = curH.sibling;
            }
        } else {
            while (curH1 != null) {
                curH.sibling = curH1;
                curH1 = curH1.sibling;
                curH = curH.sibling;
            }
        }
        curH = H.head;
        while (curH.equals(H.head) && H.head.sibling != null) {
            if (H.head.sibling.sibling != null) {
                if (H.head.degree == H.head.sibling.degree && H.head.sibling.degree == H.head.sibling.sibling.degree) {
                    curH.sibling = mergeTrees(curH.sibling, curH.sibling.sibling);
                    curH = curH.sibling;
                } else if (H.head.degree == H.head.sibling.degree) {
                    H.head = mergeTrees(H.head, H.head.sibling);
                    curH = H.head;
                } else {
                    curH = curH.sibling;
                }
            } else if (H.head.degree == H.head.sibling.degree) {
                H.head = mergeTrees(H.head, H.head.sibling);
                curH = H.head;
            } else {
                curH = curH.sibling;
            }
        }
        Node prev = H.head;
        while (curH.sibling != null) {
            if (curH.sibling.sibling != null) {
                if (curH.degree == curH.sibling.degree && curH.sibling.degree == curH.sibling.sibling.degree) {
                    curH.sibling = mergeTrees(curH.sibling, curH.sibling.sibling);
                    prev = curH;
                    curH = curH.sibling;
                } else if (curH.degree == curH.sibling.degree) {
                    prev.sibling = mergeTrees(curH, curH.sibling);
                    curH = prev.sibling;
                } else {
                    prev = curH;
                    curH = curH.sibling;
                }
            } else if (curH.degree == curH.sibling.degree) {
                prev.sibling = mergeTrees(curH, curH.sibling);
                curH = prev.sibling;
            } else {
                prev = curH;
                curH = curH.sibling;
            }
        }
        return H;
    }

    private static Node mergeTrees(Node H1, Node H2) {
        if (H1.key < H2.key) {
            H2.parent = H1;
            H1.sibling = H2.sibling;
            H2.sibling = H1.child;
            H1.child = H2;
            H1.degree++;
            return H1;
        } else {
            H1.parent = H2;
            H1.sibling = H2.child;
            H2.child = H1;
            H2.degree++;
            return H2;
        }
    }

    public void merge(BinomialHeap H) {
        BinomialHeap tmp = merge(this, H);
        this.head = tmp.head;
    }

    public void insert(int element) {
        BinomialHeap tmp = new BinomialHeap(element);
        this.merge(tmp);
    }

    public int getMin() {
        if (head == null) {
            return -1;
        }
        Node x = head;
        int min = head.key;
        while (x.sibling != null) {
            x = x.sibling;
            if (x.key < min) {
                min = x.key;
            }
        }
        return min;
    }

    private static Node nodeSiblingReverse(Node toReverse) {
        if (toReverse == null) {
            return toReverse;
        }
        Node pre = null;
        Node cur = toReverse;
        Node next = null;
        while (cur != null) {
            next = cur.sibling;
            cur.sibling = pre;
            cur.parent = null;
            pre = cur;
            cur = next;
        }
        return pre;
    }

    public Node extractMin() {
        Node cur = head;
        int min = getMin();

        if (cur.key == min) {
            head = head.sibling;
            Node toReverse = cur.child;
            cur.degree = 0;
            cur.child = null;
            cur.sibling = null;
            merge(new BinomialHeap(nodeSiblingReverse(toReverse)));
            return cur;
        }

        Node prev = cur;
        cur = cur.sibling;
        while (cur != null) {
            if (cur.key == min) {
                prev.sibling = cur.sibling;
                cur.sibling = null;
                Node toReverse = cur.child;
                cur.degree = 0;
                cur.child = null;
                merge(new BinomialHeap(nodeSiblingReverse(toReverse)));
                return cur;
            } else {
                prev = cur;
                cur = cur.sibling;
            }
        }
        return null;
    }

    public void decreaseKey(Node change, int newKey) {
        Node temp = change;
        if (temp == null)
            return;
        temp.key = newKey;
        Node tempParent = temp.parent;

        while ((tempParent != null)
                && (temp.key < tempParent.key)) {
            int z = temp.key;
            temp.key = tempParent.key;
            tempParent.key = z;

            temp = tempParent;
            tempParent = tempParent.parent;
        }
    }

    public void delete(Node toDelete) {
        decreaseKey(toDelete, -1000);
        extractMin();
    }

    public Node findNodeWithKey(int k) {
        return findNodeWithKey(k, head);
    }

    private Node findNodeWithKey(int k, Node start) {
        if (start.key == k) {
            return start;
        }
        Node possible = null;
        if (start.sibling != null) {
            possible = findNodeWithKey(k, start.sibling);
        }
        if (possible != null) {
            return possible;
        }
        if (start.child != null) {
            possible = findNodeWithKey(k, start.child);
        }
        if (possible != null) {
            return possible;
        }
        return null;
    }
}
