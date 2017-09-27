package org.matsim.munichArea.configMatsim.networkTools;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkUtils;
import org.matsim.munichArea.configMatsim.Zone2ZoneTravelDistanceListener;
import sun.nio.ch.Net;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RoadBuilder {

    private final static Logger log = Logger.getLogger(RoadBuilder.class);

    private ResourceBundle rb;
    private Network network;

    public RoadBuilder(ResourceBundle rb) {
        this.rb = rb;
    }

    public void buildRoads(Network network) {

        this.network = network;
        //create new nodes

        //read nodes
        readAndCreateNodes();

        //read links
        readAndCreateLinks();




       /* //example node n1
        double x1 = 4541826;
        double y1 = 5302196;
        Coord c1 = new Coord(x1, y1);
        Node n1 = NetworkUtils.createNode(Id.createNodeId("node1"), c1);
        network.addNode(n1);

        //example node n2
        double x2 = 4542289;
        double y2 = 5301248;
        Coord c2 = new Coord(x2, y2);
        Node n2 = NetworkUtils.createNode(Id.createNodeId("node2"), c2);
        network.addNode(n2);

        //get Node from its id
        Node exampleNode = network.getNodes().get(Id.createNodeId(265776702));
        log.info("A node has been found by its id : " + exampleNode.getId().toString());
        //remove two links because the new roads intersects them


        //get link from its id
        Link l1 = network.getLinks().get(Id.createLinkId(141381));
        log.info("A node has been found by its id : " + l1.getToNode().getId().toString());


        //get from/to nodes from a link
        Node n3 = l1.getFromNode();
        Node n4 = l1.getToNode();
        //inverse direction
        Link l2 = NetworkUtils.getConnectingLink(l1.getToNode(), l1.getFromNode());
        network.removeLink(l1.getId());
        network.removeLink(l2.getId());

        //find the intersection and add the new node
        //slope of line 1-2
        double m12 = (y2 - y1) / (x2 - x1);
        //slope of line 3-4
        double m34 = (n4.getCoord().getY() - n3.getCoord().getY()) / (n4.getCoord().getX() - n3.getCoord().getX());
        //coordinates of the intersection
        double x5 = (n3.getCoord().getY() - y1 - m34 * n3.getCoord().getX() + m12 * x1) / (m12 - m34);
        double y5 = y1 + m12 * (x5 - x1);
        //and check that it is inside the links
        if((x5-x1 > 0 && x5-x2 > 0) || (x5-x1 > 0 && x5-x2 > 0)){
            log.info("The intersection point is not in the link");
        } else {
            log.info("The intersection point is in the link");
        }

        Coord c5 = new Coord(x5, y5);
        Node n5 = NetworkUtils.createNode(Id.createNodeId("node5"), c5);
        network.addNode(n5);

        //create the new links connecting these 5 nodes
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);

        int i = 0;
        for (Node d : nodes) {
            if (!n5.equals(d)) {
                i++;
                String linkId;
                linkId = "link" + i;
                Link centerLink;
                centerLink = NetworkUtils.createLink(Id.createLinkId(linkId), n5, d, network, NetworkUtils.getEuclideanDistance(n5.getCoord(), d.getCoord()), 30, 200, 2);
                network.addLink(centerLink);
                //and the same for the return link
                i++;
                linkId = "link" + i;
                centerLink = NetworkUtils.createLink(Id.createLinkId(linkId), d, n5, network, NetworkUtils.getEuclideanDistance(n5.getCoord(), d.getCoord()), 30, 200, 2);
                network.addLink(centerLink);
            }
        }
        */
    }

    public Network getNetwork() {
        return network;
    }


    public void readAndCreateNodes(){
        String fileName = rb.getString("editor.nodes.file");
        String cvsSplitBy = ",";
        BufferedReader br = null;
        String line = "";

        try {
            int lineIndex = 0;
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null ) {
                String[] row = line.split(cvsSplitBy);
                if(lineIndex > 0) {
                    int id = Integer.parseInt(row[0]);
                    double x = Double.parseDouble(row[1]);
                    double y = Double.parseDouble(row[2]);
                    Coord coordinatesNode = new Coord(x, y);
                    Node node = NetworkUtils.createNode(Id.createNodeId(id), coordinatesNode);
                    network.addNode(node);
                }
                lineIndex++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void readAndCreateLinks(){
        String fileName = rb.getString("editor.links.file");
        String cvsSplitBy = ",";
        BufferedReader br = null;
        String line = "";

        try {
            int lineIndex = 0;
            br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null ) {
                String[] row = line.split(cvsSplitBy);
                if(lineIndex > 0) {
                    String newLinkId= row[0];
                    long fromNodeId = Long.parseLong(row[1]);
                    long toNodeId = Long.parseLong(row[2]);
                    double freeSpeed = Double.parseDouble(row[3]);
                    double capacity = Double.parseDouble(row[4]);
                    int lanes = Integer.parseInt(row[5]);
                    int oneWay = Integer.parseInt(row[6]);
                    String type = row[8];
                    int intersection = Integer.parseInt(row[9]);
                    long intLinkId = Long.parseLong(row[10]);

                    Node fromNode = network.getNodes().get(Id.createNodeId(fromNodeId));
                    Node toNode = network.getNodes().get(Id.createNodeId(toNodeId));

                    if (intersection == 0) {

                        Link link;
                        link = NetworkUtils.createLink(Id.createLinkId(newLinkId),
                                fromNode,
                                toNode,
                                network,
                                NetworkUtils.getEuclideanDistance(fromNode.getCoord(), toNode.getCoord()),
                                freeSpeed,
                                capacity,
                                lanes);

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(newLinkId + "op"),
                                toNode,
                                fromNode,
                                network,
                                NetworkUtils.getEuclideanDistance(fromNode.getCoord(), toNode.getCoord()),
                                freeSpeed,
                                capacity,
                                lanes);

                        network.addLink(link);
                    } else if (intersection == 1){




                        //find intersecting links
                        Link linkExisting = network.getLinks().get(Id.createLinkId(intLinkId));
                        Node fromNodeExisting = linkExisting.getFromNode();
                        Node toNodeExisting = linkExisting.getToNode();
                        Link linkExistingOp = NetworkUtils.getConnectingLink(toNodeExisting, fromNodeExisting);



                        //find intersection point

                        //find the intersection and add the new node
                        //1: from Node
                        double x1 = fromNode.getCoord().getX();
                        double y1 = fromNode.getCoord().getY();
                        //2: to Node
                        double x2 = toNode.getCoord().getX();
                        double y2 = toNode.getCoord().getY();

                        //3: from Node existing
                        double x3 = fromNodeExisting.getCoord().getX();
                        double y3 = fromNodeExisting.getCoord().getY();
                        //4 to Node existing
                        double x4 = toNodeExisting.getCoord().getX();
                        double y4 = toNodeExisting.getCoord().getY();



                        //slope of line between the two new nodes
                        double m12 = (y2 - y1) / (x2 - x1);
                        //slope of line 3-4
                        double m34 = (y4 - y3) / (x4 - x3);
                        //coordinates of the intersection
                        double x5 = (y3 - y1 - m34 * x3 + m12 * x1) / (m12 - m34);
                        double y5 = y1 + m12 * (x5 - x1);
                        //and check that it is inside the links

                        Coord coordIntersection = new Coord(x5, y5);
                        String intersectionNodeId = fromNodeId + "-" +  toNodeId;
                        Node nodeIntersection = NetworkUtils.createNode(Id.createNodeId(intersectionNodeId), coordIntersection);
                        network.addNode(nodeIntersection);


                        //log.info(x5);
                        //log.info(y5);
                        //create new links
                        //new network links
                        Link link;
                        link = NetworkUtils.createLink(Id.createLinkId(newLinkId + "_1"),
                                fromNode,
                                nodeIntersection,
                                network,
                                NetworkUtils.getEuclideanDistance(fromNode.getCoord(), nodeIntersection.getCoord()),
                                freeSpeed,
                                capacity,
                                lanes);

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(newLinkId + "_1op"),
                                nodeIntersection,
                                fromNode,
                                network,
                                NetworkUtils.getEuclideanDistance(fromNode.getCoord(), nodeIntersection.getCoord()),
                                freeSpeed,
                                capacity,
                                lanes);

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(newLinkId + "_2"),
                                nodeIntersection,
                                toNode,
                                network,
                                NetworkUtils.getEuclideanDistance(nodeIntersection.getCoord(), toNode.getCoord()),
                                freeSpeed,
                                capacity,
                                lanes);

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(newLinkId + "_2op"),
                                toNode,
                                nodeIntersection,
                                network,
                                NetworkUtils.getEuclideanDistance(nodeIntersection.getCoord(), toNode.getCoord()),
                                freeSpeed,
                                capacity,
                                lanes);

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(linkExisting + "_3"),
                                fromNodeExisting,
                                nodeIntersection,
                                network,
                                NetworkUtils.getEuclideanDistance(fromNodeExisting.getCoord(), nodeIntersection.getCoord()),
                                linkExisting.getFreespeed(),
                                linkExisting.getCapacity(),
                                linkExisting.getNumberOfLanes());

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(linkExistingOp + "_3op"),
                                nodeIntersection,
                                fromNodeExisting,
                                network,
                                NetworkUtils.getEuclideanDistance(fromNodeExisting.getCoord(), nodeIntersection.getCoord()),
                                linkExistingOp.getFreespeed(),
                                linkExistingOp.getCapacity(),
                                linkExistingOp.getNumberOfLanes());

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(linkExisting + "_4"),
                                nodeIntersection,
                                toNodeExisting,
                                network,
                                NetworkUtils.getEuclideanDistance(nodeIntersection.getCoord(), toNodeExisting.getCoord()),
                                linkExisting.getFreespeed(),
                                linkExisting.getCapacity(),
                                linkExisting.getNumberOfLanes());

                        network.addLink(link);

                        link = NetworkUtils.createLink(Id.createLinkId(linkExistingOp + "_4op"),
                                toNodeExisting,
                                nodeIntersection,
                                network,
                                NetworkUtils.getEuclideanDistance(nodeIntersection.getCoord(), toNodeExisting.getCoord()),
                                linkExistingOp.getFreespeed(),
                                linkExistingOp.getCapacity(),
                                linkExistingOp.getNumberOfLanes());

                        network.addLink(link);

                        //remove old links
                        network.removeLink(linkExisting.getId());
                        network.removeLink(linkExistingOp.getId());

                    }

                }
                lineIndex++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
